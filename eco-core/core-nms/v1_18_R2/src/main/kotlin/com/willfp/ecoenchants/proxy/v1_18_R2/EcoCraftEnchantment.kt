package com.willfp.ecoenchants.proxy.v1_18_R2

import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.VanillaEnchantmentData
import net.minecraft.world.item.enchantment.Enchantment
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_18_R2.enchantments.CraftEnchantment


class EcoCraftEnchantment(
    target: Enchantment,
    private val data: VanillaEnchantmentData
) : CraftEnchantment(target) {
    override fun getMaxLevel(): Int = data.maxLevel ?: super.getMaxLevel()

    override fun conflictsWith(other: org.bukkit.enchantments.Enchantment): Boolean {
        if (other is EcoEnchant) {
            return other.conflictsWith(this)
        }

        return data.conflicts?.contains(other.key) ?: super.conflictsWith(other)
    }

    @Suppress("UNCHECKED_CAST")
    fun register() {
        org.bukkit.enchantments.Enchantment::class.java.getDeclaredField("byKey")
            .apply {
                isAccessible = true
                (get(null) as MutableMap<NamespacedKey, org.bukkit.enchantments.Enchantment>).apply { remove(key) }
            }

        org.bukkit.enchantments.Enchantment::class.java.getDeclaredField("byName")
            .apply {
                isAccessible = true
                (get(null) as MutableMap<String, org.bukkit.enchantments.Enchantment>).apply { remove(name) }
            }

        org.bukkit.enchantments.Enchantment::class.java.getDeclaredField("acceptingNew")
            .apply {
                isAccessible = true
                set(null, true)
            }

        org.bukkit.enchantments.Enchantment.registerEnchantment(this)
    }
}
