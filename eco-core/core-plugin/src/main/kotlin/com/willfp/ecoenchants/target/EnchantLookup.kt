package com.willfp.ecoenchants.target

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.items.HashedItem
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.enchants.EcoEnchantLevel
import com.willfp.ecoenchants.enchants.FoundEcoEnchantLevel
import com.willfp.libreforge.ItemProvidedHolder
import com.willfp.libreforge.slot.SlotType
import com.willfp.libreforge.slot.SlotTypes
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

// The Int is the inventory slot ID
typealias SlotProvider = (Player) -> Map<ItemInNumericSlot, ItemInSlot>

data class ItemInSlot internal constructor(
    val item: ItemStack,
    val slot: Collection<SlotType>
) {
    constructor(
        item: ItemStack,
        slot: SlotType
    ) : this(item, listOf(slot))
}

data class ItemInNumericSlot internal constructor(
    val item: ItemStack,
    val slot: Int
) {
    override fun hashCode(): Int {
        return HashedItem.of(item).hash * (slot + 1)
    }
}

private data class HeldEnchant(
    val enchant: EcoEnchant,
    val level: Int
)

object EnchantLookup {
    private val slotProviders = mutableSetOf<SlotProvider>()

    private val itemCache = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build<Player, Map<ItemInNumericSlot, ItemInSlot>>()

    private val enchantCache = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build<Player, Map<ItemInNumericSlot, Collection<HeldEnchant>>>()

    // Higher frequency cache as less intensive
    private val enchantLevelCache = Caffeine.newBuilder()
        .expireAfterWrite(200, TimeUnit.MILLISECONDS)
        .build<Player, Map<ItemInNumericSlot, Map<EcoEnchant, Int>>>()

    @JvmStatic
    fun registerProvider(provider: SlotProvider) {
        slotProviders.add {
            val found = mutableMapOf<ItemInNumericSlot, ItemInSlot>()
            for ((slot, inSlot) in provider(it)) {
                found[slot] = inSlot
            }
            found
        }
    }

    private fun provide(player: Player): Map<ItemInNumericSlot, ItemInSlot> {
        return itemCache.get(player) {
            val found = mutableMapOf<ItemInNumericSlot, ItemInSlot>()

            for (provider in slotProviders) {
                val fromProvider = provider(player)
                for ((slot, item) in fromProvider) {
                    // Basically a multimap
                    val current = found[slot]

                    if (current == null) {
                        found[slot] = item
                    } else {
                        found[slot] = ItemInSlot(item.item, listOf(current.slot, item.slot).flatten())
                    }
                }
            }

            found
        }
    }

    /**
     * The inventory slot IDs mapped to HeldEnchants found in that slot.
     */
    private val Player.slotHeldEnchants: Map<ItemInNumericSlot, Collection<HeldEnchant>>
        get() {
            return enchantCache.get(this) {
                val found = mutableMapOf<ItemInNumericSlot, MutableCollection<HeldEnchant>>()

                for ((slotID, inSlot) in provide(this)) {
                    val (item, slot) = inSlot

                    val enchants = item.fast().enchants

                    for ((enchant, level) in enchants) {
                        if (enchant !is EcoEnchant) {
                            continue
                        }

                        if (slot.none { it in enchant.slots }) {
                            continue
                        }

                        val held = HeldEnchant(enchant, level)

                        // Prevent repeating slot IDs found in multiple TargetSlots (e.g. HANDS and ANY)
                        if (held in found.getOrDefault(slotID, mutableListOf())) {
                            continue
                        }

                        // Basically a multimap
                        found[slotID] = (found.getOrDefault(slotID, mutableListOf())
                                + HeldEnchant(enchant, level)).toMutableList()
                    }
                }

                found
            }
        }

    /**
     * Slot IDs mapped to HeldEnchants found in that slot.
     */
    private val Player.slotIDHeldEnchants: Map<Int, Collection<HeldEnchant>>
        get() {
            return this.slotHeldEnchants.mapKeys { it.key.slot }
        }

    /**
     * All slots holding EcoEnchants mapped to their IDs, regardless of conditions.
     */
    val Player.heldEnchantsInSlots: Map<ItemInNumericSlot, Map<EcoEnchant, Int>>
        get() {
            return enchantLevelCache.get(this) {
                val found = mutableMapOf<ItemInNumericSlot, MutableMap<EcoEnchant, Int>>()

                for ((inSlot, enchants) in this.slotHeldEnchants) {
                    val map = mutableMapOf<EcoEnchant, Int>()
                    for ((enchant, level) in enchants) {
                        map[enchant] = level
                    }
                    found[inSlot] = map
                }

                found
            }
        }

    /**
     * All EcoEnchants mapped to their IDs, regardless of conditions.
     */
    val Player.heldEnchants: Map<EcoEnchant, Int>
        get() {
            val found = mutableMapOf<EcoEnchant, Int>()
            for ((_, enchants) in this.heldEnchantsInSlots) {
                for ((enchant, level) in enchants) {
                    found[enchant] = level
                }
            }

            return found
        }

    /**
     * All slots mapped to EcoEnchants mapped to their IDs, respecting conditions.
     */
    val Player.activeEnchantsInSlots: Map<ItemInNumericSlot, Map<EcoEnchant, Int>>
        get() {
            val found = mutableMapOf<ItemInNumericSlot, MutableMap<EcoEnchant, Int>>()
            for ((slot, enchants) in this.heldEnchantsInSlots) {
                val inSlot = mutableMapOf<EcoEnchant, Int>()
                for ((enchant, level) in enchants) {
                    val enchantLevel = enchant.getLevel(level)
                    val providedHolder = ItemProvidedHolder(enchantLevel, slot.item)
                    if (enchantLevel.conditions.areMet(this, providedHolder)) {
                        inSlot[enchant] = level
                    }
                }
                found[slot] = inSlot
            }

            return found
        }

    /**
     * All EcoEnchants mapped to their IDs, respecting conditions.
     */
    val Player.activeEnchants: Map<EcoEnchant, Int>
        get() {
            val found = mutableMapOf<EcoEnchant, Int>()
            for ((_, enchants) in this.activeEnchantsInSlots) {
                for ((enchant, level) in enchants) {
                    found[enchant] = level
                }
            }

            return found
        }

    /**
     * Get the enchantment level on a player, ignoring conditions.
     *
     * @return The level, or 0 if not found.
     */
    fun Player.getEnchantLevel(enchant: EcoEnchant): Int {
        return this.heldEnchants[enchant] ?: 0
    }

    /**
     * Get the enchantment level on a player, respecting.
     *
     * @return The level, or 0 if not found.
     */
    fun Player.getActiveEnchantLevel(enchant: EcoEnchant): Int {
        return this.activeEnchants[enchant] ?: 0
    }

    /**
     * Get the enchantment level on a player in a specific slot, ignoring conditions.
     *
     * @return The level, or 0 if not found.
     */
    fun Player.getEnchantLevelInSlot(enchant: EcoEnchant, slot: Int): Int {
        val inSlot = this.slotIDHeldEnchants[slot] ?: return 0
        val heldEnchant = inSlot.firstOrNull { it.enchant == enchant } ?: return 0
        return heldEnchant.level
    }

    /**
     * Get the enchantment level on a player in a specific slot, respecting conditions.
     *
     * @return The level, or 0 if not found.
     */
    fun Player.getActiveEnchantLevelInSlot(enchant: EcoEnchant, slot: Int): Int {
        val level = getEnchantLevelInSlot(enchant, slot)

        if (level == 0) {
            return 0
        }

        val enchantLevel = enchant.getLevel(level)
        val item = this.inventory.getItem(slot) ?: return 0
        val providedHolder = ItemProvidedHolder(enchantLevel, item)

        if (!enchantLevel.conditions.areMet(this, providedHolder)) {
            return 0
        }

        return level
    }

    /**
     * Get if a player has an enchantment, ignoring conditions.
     *
     * @return If the player has the enchantment.
     */
    fun Player.hasEnchant(enchant: EcoEnchant): Boolean {
        return this.getEnchantLevel(enchant) > 0
    }

    /**
     * Get if a player has an enchantment, respecting conditions.
     *
     * @return If the player has the enchantment.
     */
    fun Player.hasEnchantActive(enchant: EcoEnchant): Boolean {
        return this.getActiveEnchantLevel(enchant) > 0
    }

    /**
     * Get if a player has an enchantment in a slot, ignoring conditions.
     *
     * @return If the player has the enchantment.
     */
    fun Player.hasEnchantInSlot(enchant: EcoEnchant, slot: Int): Boolean {
        return this.getEnchantLevelInSlot(enchant, slot) > 0
    }

    /**
     * Get if a player has an enchantment in a slot, respecting conditions.
     *
     * @return If the player has the enchantment.
     */
    fun Player.hasEnchantActiveInSlot(enchant: EcoEnchant, slot: Int): Boolean {
        return this.getActiveEnchantLevelInSlot(enchant, slot) > 0
    }

    val Player.heldEnchantLevels: List<ItemProvidedHolder>
        get() {
            val found = mutableListOf<ItemProvidedHolder>()

            for ((slot, enchants) in this.heldEnchantsInSlots) {
                for ((enchant, level) in enchants) {
                    found.add(ItemProvidedHolder(enchant.getLevel(level), slot.item))
                }
            }

            // This is such a fucking disgusting way of implementing %active_level%,
            // and it's probably quite slow too.
            return if (EcoEnchantsPlugin.instance.configYml.getBool("extra-placeholders.active-level")) {
                found.map {
                    val level = it.holder as EcoEnchantLevel

                    ItemProvidedHolder(
                        FoundEcoEnchantLevel(level, this.getActiveEnchantLevel(level.enchant)),
                        it.provider
                    )
                }
            } else {
                found
            }
        }

    /**
     * Clear item and enchant cache.
     */
    fun Player.clearEnchantCache() {
        itemCache.invalidate(player)
        enchantCache.invalidate(player)
        enchantLevelCache.invalidate(player)
    }

    init {
        fun createProvider(slot: SlotType): SlotProvider {
            return { player: Player ->
                val found = mutableMapOf<ItemInNumericSlot, ItemInSlot>()

                for (slotID in slot.getItemSlots(player)) {
                    val item = player.inventory.getItem(slotID) ?: continue
                    found[
                            ItemInNumericSlot(item, slotID)
                    ] = ItemInSlot(item, slot)
                }

                found
            }
        }

        for (slot in SlotTypes.values()) {
            registerProvider(createProvider(slot))
        }
    }
}
