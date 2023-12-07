package com.willfp.ecoenchants.enchant.registration.modern

import com.willfp.ecoenchants.enchant.registration.EnchantmentRegisterer

interface ModernEnchantmentRegistererProxy : EnchantmentRegisterer {
    /**
     * Replace the bukkit enchantment registry with the new EcoEnchants one.
     */
    fun replaceRegistry()
}
