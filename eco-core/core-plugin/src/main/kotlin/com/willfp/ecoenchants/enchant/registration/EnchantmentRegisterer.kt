package com.willfp.ecoenchants.enchant.registration

import com.willfp.ecoenchants.enchant.EcoEnchant

interface EnchantmentRegisterer {
    fun register(enchant: EcoEnchant)

    fun unregister(enchant: EcoEnchant)
}