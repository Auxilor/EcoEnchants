package com.willfp.ecoenchants.integrations

import com.willfp.eco.core.integrations.Integration
import com.willfp.ecoenchants.enchant.EcoEnchant


interface EnchantRegistrationIntegration : Integration {
    fun registerEnchants()

    fun removeEnchant(enchantment: EcoEnchant) {

    }
}

object EnchantRegistrations {
    private val registered = mutableSetOf<EnchantRegistrationIntegration>()

    fun register(integration: EnchantRegistrationIntegration) {
        registered.add(integration)
    }

    internal fun registerEnchantments() {
        for (integration in registered) {
            runCatching { integration.registerEnchants() }
        }
    }

    fun removeEnchant(enchantment: EcoEnchant) {
        for (integration in registered) {
            runCatching { integration.removeEnchant(enchantment) }
        }
    }
}
