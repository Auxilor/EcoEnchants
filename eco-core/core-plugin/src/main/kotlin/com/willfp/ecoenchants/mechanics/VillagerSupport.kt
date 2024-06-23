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
import org.bukkit.event.entity.VillagerAcquireTradeEvent
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import kotlin.math.ceil


class VillagerSupport(
    private val plugin: EcoEnchantsPlugin
) : Listener {
    @EventHandler
    fun onTrade(event: VillagerAcquireTradeEvent) {
        if (!this.plugin.configYml.getBool("villager.enabled")) {
            return
        }

        val result = event.recipe.result.clone()

        if (!result.isEnchantable) {
            return
        }

        val enchants = result.fast().getEnchants(true)

        if (NumberUtils.randFloat(0.0, 100.0) < plugin.configYml.getDouble("villager.pass-through-chance")) {
            return
        }

        var multiplier = 0.01

        if (result.type == Material.ENCHANTED_BOOK) {
            multiplier *= plugin.configYml.getDouble("villager.book-multiplier")
        }

        val enchantments = EcoEnchants.values().shuffled()

        for (enchantment in enchantments) {
            if (!enchantment.isObtainableThroughTrading) {
                continue
            }

            if (!enchantment.canEnchantItem(result)) {
                continue
            }

            if (NumberUtils.randFloat(0.0, 1.0) > enchantment.enchantmentRarity.villagerChance * multiplier) {
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

            val levelPart1 = event.recipe.ingredients[0].amount / 64.0
            val levelPart2 = NumberUtils.triangularDistribution(0.0, 1.0, levelPart1)
            val levelPart3 = NumberUtils.bias(levelPart2, enchantment.type.highLevelBias)
            val level = ceil(levelPart3 * maxLevel).coerceIn(1.0..maxLevel.toDouble()).toInt()

            multiplier /= this.plugin.configYml.getDouble("villager.reduction")

            if (result.type == Material.ENCHANTED_BOOK) {
                // Only allow one enchantment
                enchants.clear()
                enchants[enchantment.enchantment] = level
                break
            } else {
                enchants[enchantment.enchantment] = level
            }
        }

        val meta = result.itemMeta
        if (meta is EnchantmentStorageMeta) {
            // Remove existing enchants
            for (enchant in meta.storedEnchants.keys) {
                meta.removeStoredEnchant(enchant)
            }

            for ((enchant, level) in enchants) {
                meta.addStoredEnchant(enchant, level, true)
            }
        } else {
            for ((enchant, level) in enchants) {
                meta.removeEnchant(enchant)
                meta.addEnchant(enchant, level, true)
            }
        }
        result.itemMeta = meta

        val recipe = MerchantRecipe(
            result,
            event.recipe.uses,
            event.recipe.maxUses,
            event.recipe.hasExperienceReward(),
            event.recipe.villagerExperience,
            event.recipe.priceMultiplier
        )

        recipe.ingredients = event.recipe.ingredients
        event.recipe = recipe
    }
}
