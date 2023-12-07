package com.willfp.ecoenchants.enchant.registration

import com.willfp.ecoenchants.enchant.EcoEnchant
import org.bukkit.enchantments.Enchantment

interface EnchantmentRegisterer {
    fun register(enchant: EcoEnchant): Enchantment

    fun unregister(enchant: EcoEnchant)
}
