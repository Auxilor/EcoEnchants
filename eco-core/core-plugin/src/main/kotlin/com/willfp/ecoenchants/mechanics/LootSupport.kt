package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.NumberUtils
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.conflictsWithDeep
import com.willfp.ecoenchants.target.EnchantmentTargets.isEnchantable
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.LootGenerateEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import kotlin.math.ceil

class LootSupport(
    private val plugin: EcoEnchantsPlugin
) : Listener {
    @EventHandler
    fun onGenerate(event: LootGenerateEvent) {
        if (!this.plugin.configYml.getBool("loot.enabled")) {
            return
        }

        for (itemStack in event.loot) {
            modifyItem(itemStack)
        }
    }

    private fun modifyItem(item: ItemStack) {
        if (!item.isEnchantable) {
            return
        }

        val enchants = item.fast().getEnchants(true)

        var multiplier = 0.01

        if (item.type == Material.ENCHANTED_BOOK) {
            multiplier *= plugin.configYml.getDouble("loot.book-multiplier")
        }

        val enchantments = EcoEnchants.values().shuffled()

        for (enchantment in enchantments) {
            if (!enchantment.isObtainableThroughDiscovery) {
                continue
            }

            if (!enchantment.canEnchantItem(item)) {
                continue
            }

            if (NumberUtils.randFloat(0.0, 1.0) > enchantment.enchantmentRarity.lootChance * multiplier) {
                continue
            }

            if (enchants.any { (it, _) -> enchantment.enchantment.conflictsWithDeep(it) }) {
                continue
            }

            if (enchants.size > plugin.configYml.getInt("anvil.enchant-limit").infiniteIfNegative()) {
                break
            }

            if (
                enchants.keys.filterIsInstance<EcoEnchant>()
                    .count { it.type == enchantment.type } >= enchantment.type.limit
            ) {
                continue
            }

            val maxLevel = enchantment.maximumLevel

            val levelPart1 = NumberUtils.bias(NumberUtils.randFloat(0.7, 1.0), enchantment.type.highLevelBias)
            val levelPart2 = NumberUtils.triangularDistribution(0.0, 1.0, levelPart1)
            val level = ceil(levelPart2 * maxLevel).coerceIn(1.0..maxLevel.toDouble()).toInt()

            multiplier /= this.plugin.configYml.getDouble("villager.reduction")

            enchants[enchantment.enchantment] = level
        }

        val meta = item.itemMeta
        if (meta is EnchantmentStorageMeta) {
            enchants.forEach { (enchant, level) -> meta.addStoredEnchant(enchant, level, true) }
        } else {
            enchants.forEach { (enchant, level) -> meta.addEnchant(enchant, level, true) }
        }
        item.itemMeta = meta
    }
}
