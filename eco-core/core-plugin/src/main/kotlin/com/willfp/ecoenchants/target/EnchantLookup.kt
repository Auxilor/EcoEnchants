package com.willfp.ecoenchants.target

import com.github.benmanes.caffeine.cache.Caffeine
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.enchants.EcoEnchantLevel
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

typealias SlotProvider = (Player) -> Map<ItemStack?, TargetSlot?>

object EnchantLookup {
    private val plugin = EcoEnchantsPlugin.instance

    private val slotProviders = mutableSetOf<(Player) -> Map<ItemStack, TargetSlot>>()

    private val itemCache = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build<Player, Map<ItemStack, TargetSlot>>()

    private val enchantCache = Caffeine.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .build<Player, Map<EcoEnchant, Int>>()

    @JvmStatic
    fun registerProvider(provider: SlotProvider) {
        slotProviders.add {
            val found = mutableMapOf<ItemStack, TargetSlot>()
            for ((item, slot) in provider(it)) {
                if (item != null && slot != null) {
                    found[item] = slot
                }
            }
            found
        }
    }

    private fun provide(player: Player): Map<ItemStack, TargetSlot> {
        return itemCache.get(player) {
            val found = mutableMapOf<ItemStack, TargetSlot>()
            for (provider in slotProviders) {
                found.putAll(provider(player))
            }

            found
        }
    }

    val Player.heldEnchants: Map<EcoEnchant, Int>
        get() {
            return enchantCache.get(player) {
                val found = mutableMapOf<EcoEnchant, Int>()

                for ((itemStack, slot) in provide(this)) {
                    val enchants = itemStack.fast().enchants

                    for ((enchant, level) in enchants) {
                        if (enchant !is EcoEnchant) {
                            continue
                        }

                        if (enchant.slots.contains(slot) || slot == TargetSlot.ANY) {
                            found[enchant] = found.getOrDefault(enchant, 0) + level
                        }
                    }
                }

                found
            }
        }

    val Player.activeEnchants: Map<EcoEnchant, Int>
        get() {
            return this.heldEnchants.filter { (enchant, level) ->
                enchant.getLevel(level).conditions.all { it.isMet(this) }
            }
        }

    fun Player.getEnchantLevel(enchant: EcoEnchant): Int {
        return this.heldEnchants[enchant] ?: 0
    }

    fun Player.getActiveEnchantLevel(enchant: EcoEnchant): Int {
        return this.activeEnchants[enchant] ?: 0
    }

    fun Player.hasEnchant(enchant: EcoEnchant): Boolean {
        return this.getEnchantLevel(enchant) > 0
    }

    fun Player.hasEnchantActive(enchant: EcoEnchant): Boolean {
        return this.getActiveEnchantLevel(enchant) > 0
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
    }

    init {
        registerProvider {
            mapOf(
                Pair(
                    it.inventory.itemInMainHand,
                    TargetSlot.HANDS
                )
            )
        }

        if (!plugin.configYml.getBool("no-offhand")) {
            registerProvider {
                mapOf(
                    Pair(
                        it.inventory.itemInOffHand,
                        TargetSlot.HANDS
                    )
                )
            }
        }

        registerProvider {
            val items = mutableMapOf<ItemStack?, TargetSlot?>()
            for (stack in it.inventory.armorContents) {
                items[stack] = TargetSlot.ARMOR
            }
            items
        }
    }
}
