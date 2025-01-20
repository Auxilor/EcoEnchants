package com.willfp.ecoenchants.proxy.v1_21_4.registration

import com.willfp.ecoenchants.enchant.vanillaEnchantmentData
import net.minecraft.core.Holder
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.enchantments.CraftEnchantment

class ModifiedVanillaCraftEnchantment(
    private val key: NamespacedKey,
    target: Enchantment,
    holder: Holder<Enchantment>
) : CraftEnchantment(holder) {
    override fun getMaxLevel(): Int = this.vanillaEnchantmentData?.maxLevel ?: super.getMaxLevel()

    override fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        val otherConflicts = when (other) {
            is ModifiedVanillaCraftEnchantment -> other.vanillaEnchantmentData?.conflicts?.contains(this.key) == true
            else -> other.conflictsWith(this)
        }

        return this.vanillaEnchantmentData?.conflicts?.contains(other.key) ?: super.conflictsWith(other)
                || otherConflicts
    }
}
