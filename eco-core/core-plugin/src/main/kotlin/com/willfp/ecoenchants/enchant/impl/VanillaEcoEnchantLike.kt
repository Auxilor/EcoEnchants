package com.willfp.ecoenchants.enchant.impl

import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.EcoEnchantLike
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.enchantments.Enchantment
import java.util.Objects

class VanillaEcoEnchantLike(
    override val enchantment: Enchantment,
    override val plugin: EcoEnchantsPlugin
) : EcoEnchantLike {
    override val config = plugin.vanillaEnchantsYml.getSubsection(enchantment.key.key)

    override val maximumLevel
        get() = enchantment.maxLevel

    override val type: EnchantmentType =
        EnchantmentTypes[plugin.vanillaEnchantsYml.getString("${enchantment.key.key}.type")]
            ?: EnchantmentTypes.values().first()

    override val enchantmentRarity: EnchantmentRarity =
        EnchantmentRarities[plugin.vanillaEnchantsYml.getString("${enchantment.key.key}.rarity")]
            ?: EnchantmentRarities.values().first()

    override val rawDisplayName = plugin.vanillaEnchantsYml.getString("${enchantment.key.key}.name")

    override fun equals(other: Any?): Boolean {
        if (other !is VanillaEcoEnchantLike) {
            return false
        }

        return this.enchantment == other.enchantment
    }

    override fun hashCode(): Int {
        return Objects.hash(this.enchantment)
    }
}
