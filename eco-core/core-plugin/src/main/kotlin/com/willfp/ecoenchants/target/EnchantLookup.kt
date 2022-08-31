package com.willfp.ecoenchants.target

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.enchants.EcoEnchantLevel
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

// The Int is the inventory slot ID
typealias SlotProvider = (Player) -> Map<Int, ItemInSlot>

data class ItemInSlot internal constructor(
    val item: ItemStack,
    val slot: Collection<TargetSlot>
) {
    constructor(
        item: ItemStack,
        slot: TargetSlot
    ) : this(item, listOf(slot))
}

private data class HeldEnchant(
    val enchant: EcoEnchant,
    val level: Int
)

object EnchantLookup {
    private val slotProviders = mutableSetOf<SlotProvider>()

    private val itemCache = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build<Player, Map<Int, ItemInSlot>>()

    private val enchantCache = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build<Player, Map<Int, Collection<HeldEnchant>>>()

    // Higher frequency cache as less intensive
    private val enchantLevelCache = Caffeine.newBuilder()
        .expireAfterWrite(200, TimeUnit.MILLISECONDS)
        .build<Player, Map<EcoEnchant, Int>>()

    @JvmStatic
    fun registerProvider(provider: SlotProvider) {
        slotProviders.add {
            val found = mutableMapOf<Int, ItemInSlot>()
            for ((slot, inSlot) in provider(it)) {
                found[slot] = inSlot
            }
            found
        }
    }

    private fun provide(player: Player): Map<Int, ItemInSlot> {
        return itemCache.get(player) {
            val found = mutableMapOf<Int, ItemInSlot>()
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
    private val Player.slotHeldEnchants: Map<Int, Collection<HeldEnchant>>
        get() {
            return enchantCache.get(this) {
                val found = mutableMapOf<Int, MutableCollection<HeldEnchant>>()

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
     * All EcoEnchants mapped to their IDs, regardless of conditions.
     */
    val Player.heldEnchants: Map<EcoEnchant, Int>
        get() {
            return enchantLevelCache.get(this) {
                val found = mutableMapOf<EcoEnchant, Int>()

                for ((enchant, level) in it.slotHeldEnchants.values.flatten()) {
                    found[enchant] = found.getOrDefault(enchant, 0) + level
                }

                found
            }
        }

    /**
     * All EcoEnchants mapped to their IDs, respecting conditions.
     */
    val Player.activeEnchants: Map<EcoEnchant, Int>
        get() {
            return this.heldEnchants.filter { (enchant, level) ->
                enchant.getLevel(level).conditions.all { it.isMet(this) }
            }
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
        val inSlot = this.slotHeldEnchants[slot] ?: return 0
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

        if (enchant.getLevel(level).conditions.any { !it.isMet(this) }) {
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

    val Player.heldEnchantLevels: List<EcoEnchantLevel>
        get() = this.heldEnchants
            .map { (enchant, level) -> enchant.getLevel(level) }

    /**
     * Clear item and enchant cache.
     */
    fun Player.clearEnchantCache() {
        itemCache.invalidate(player)
        enchantCache.invalidate(player)
        enchantLevelCache.invalidate(player)
    }

    init {
        fun createProvider(slot: TargetSlot): SlotProvider {
            return { player: Player ->
                val found = mutableMapOf<Int, ItemInSlot>()

                for (slotID in slot.getItemSlots(player)) {
                    val item = player.inventory.getItem(slotID) ?: continue
                    found[slotID] = ItemInSlot(item, slot)
                }

                found
            }
        }

        for (slot in TargetSlot.values()) {
            registerProvider(createProvider(slot))
        }
    }
}
