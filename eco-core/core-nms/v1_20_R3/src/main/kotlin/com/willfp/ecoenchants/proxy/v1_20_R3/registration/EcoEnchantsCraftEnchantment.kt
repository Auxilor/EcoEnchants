package com.willfp.ecoenchants.proxy.v1_20_R3.registration

import com.willfp.eco.util.toComponent
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.impl.EcoEnchantBase
import io.papermc.paper.enchantments.EnchantmentRarity
import net.kyori.adventure.text.Component
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.craftbukkit.v1_20_R3.enchantments.CraftEnchantment
import org.bukkit.enchantments.EnchantmentTarget
import org.bukkit.entity.EntityCategory
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.Objects

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
