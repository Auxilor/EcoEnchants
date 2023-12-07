package com.willfp.ecoenchants.enchant.registration.legacy

import com.willfp.eco.util.StringUtils
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.wrap
import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import org.bukkit.enchantments.Enchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot

@Suppress("DEPRECATION")
class LegacyDelegatedEnchantment(
    private val enchant: EcoEnchant
) : Enchantment(enchant.key), EcoEnchant by enchant {
    override fun translationKey(): String {
        return "ecoenchants:enchantment.$id"
    }

    @Deprecated(
        message = "getName is a legacy Spigot API",
        replaceWith = ReplaceWith("this.displayName(level)")
    )
    override fun getName(): String = this.id.uppercase()

    override fun getMaxLevel(): Int {
        return maxLevel
    }

    override fun getStartLevel(): Int {
        return 1
    }

    @Deprecated(
        message = "getItemTargets is an incompatible Spigot API",
        replaceWith = ReplaceWith("this.targets")
    )
    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.ALL

    @Deprecated(
        message = "Treasure enchantments do not exist in EcoEnchants",
        replaceWith = ReplaceWith("this.isEnchantable")
    )
    override fun isTreasure(): Boolean = !isEnchantable

    @Deprecated(
        message = "Use EnchantmentType instead",
        replaceWith = ReplaceWith("type.id")
    )
    override fun isCursed(): Boolean {
        return false
    }

    override fun displayName(level: Int): Component {
        return StringUtils.toComponent(this.wrap().getFormattedName(level))
    }

    override fun isTradeable(): Boolean {
        return isTradeable
    }

    override fun isDiscoverable(): Boolean {
        return isDiscoverable
    }

    @Deprecated(
        message = "EcoEnchants uses a custom system for enchantment rarity",
        replaceWith = ReplaceWith("this.enchantRarity")
    )
    override fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    @Deprecated(
        message = "EcoEnchants do not have damage increase, this method is for sharpness/boa/smite",
        replaceWith = ReplaceWith("0.0f")
    )
    override fun getDamageIncrease(level: Int, entityCategory: EntityCategory): Float = 0.0f

    @Deprecated(
        message = "getActiveSlots is an incompatible Paper API",
        replaceWith = ReplaceWith("this.slots")
    )
    override fun getActiveSlots() = emptySet<EquipmentSlot>()
}
