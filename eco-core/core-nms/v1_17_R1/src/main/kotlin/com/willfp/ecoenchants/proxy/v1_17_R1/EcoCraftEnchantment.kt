package com.willfp.ecoenchants.proxy.v1_17_R1

import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.enchants.EcoEnchants
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_17_R1.enchantments.CraftEnchantment

class EcoCraftEnchantment(
    target: Enchantment,
    private val _maxLevel: Int,
    private val conflicts: Collection<NamespacedKey>
) : CraftEnchantment(target) {
    override fun getMaxLevel(): Int = _maxLevel

    override fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        if (other is EcoEnchant) {
            return other.conflictsWith(this)
        }

        return conflicts.contains(other.key)
    }

    fun register() {
        EcoEnchants.register(this)
    }
}
