package com.willfp.ecoenchants.enchant.registration

import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.impl.EcoEnchantBase
import org.bukkit.enchantments.Enchantment

interface EnchantmentRegisterer {
    fun register(enchant: EcoEnchantBase): Enchantment

    fun unregister(enchant: EcoEnchant)
}
