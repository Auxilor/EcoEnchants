package com.willfp.ecoenchants.enchant.registration

interface ModernEnchantmentRegistererProxy : EnchantmentRegisterer {
    /**
     * Replace the bukkit enchantment registry with the new EcoEnchants one.
     */
    fun replaceRegistry()

    /**
     * Re-freeze the registry.
     */
    fun freezeRegistry()
}