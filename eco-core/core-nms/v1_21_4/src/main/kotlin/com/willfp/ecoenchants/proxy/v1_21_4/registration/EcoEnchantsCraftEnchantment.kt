package com.willfp.ecoenchants.proxy.v1_21_4.registration

import com.willfp.eco.util.toComponent
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.impl.EcoEnchantBase
import net.kyori.adventure.text.Component
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.craftbukkit.enchantments.CraftEnchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.inventory.ItemStack

class EcoEnchantsCraftEnchantment(
    private val enchant: EcoEnchantBase,
    nmsEnchantment: Enchantment
) : CraftEnchantment(enchant.enchantmentKey, nmsEnchantment), EcoEnchant by enchant {
    init {
        enchant.enchantment = this
    }

    override fun onRegister() {
        // Fix for hardcoded enchantments
        if (plugin.isLoaded) {
            enchant.onRegister()
        }
    }

    override fun onRemove() {
        enchant.onRemove()
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return enchant.canEnchantItem(item)
    }

    override fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        return enchant.conflictsWith(other)
    }

    @Deprecated(
        message = "EcoEnchants enchantments are not translatable",
        replaceWith = ReplaceWith("this.displayName(level)")
    )
    override fun translationKey(): String {
        return "ecoenchants:enchantment.$id"
    }

    @Deprecated(
        message = "getName is a legacy Spigot API",
        replaceWith = ReplaceWith("this.displayName(level)")
    )
    override fun getName(): String = this.id.uppercase()
    override fun getMaxLevel(): Int = enchant.maximumLevel

    override fun getStartLevel(): Int = 1

    @Deprecated(
        message = "getItemTargets is an incompatible Spigot API",
        replaceWith = ReplaceWith("this.targets")
    )
    @Suppress("DEPRECATION")
    override fun getItemTarget(): EnchantmentTarget = EnchantmentTarget.ALL

    @Deprecated(
        message = "Treasure enchantments do not exist in EcoEnchants",
        replaceWith = ReplaceWith("this.isEnchantable")
    )
    override fun isTreasure(): Boolean = !enchant.isObtainableThroughEnchanting

    @Deprecated(
        message = "Use EnchantmentType instead",
        replaceWith = ReplaceWith("type.id")
    )
    override fun isCursed(): Boolean {
        return false
    }

    override fun displayName(level: Int): Component {
        return enchant.getFormattedName(level).toComponent()
    }

    override fun isTradeable(): Boolean {
        return enchant.isObtainableThroughTrading
    }

    override fun isDiscoverable(): Boolean {
        return enchant.isObtainableThroughDiscovery
    }

    override fun getMinModifiedCost(level: Int): Int {
        return Int.MAX_VALUE
    }

    override fun getMaxModifiedCost(level: Int): Int {
        return Int.MAX_VALUE
    }

    override fun equals(other: Any?): Boolean {
        return other is EcoEnchant && this.enchantmentKey == other.enchantmentKey
    }

    override fun hashCode(): Int {
        return this.enchantmentKey.hashCode()
    }

    override fun toString(): String {
        return "EcoEnchantsCraftEnchantment(key=$key)"
    }
}
