package com.willfp.ecoenchants.integrations

import com.willfp.ecoenchants.enchant.EcoEnchant
import wiki.ecohub.core.plugin.BaseEcoPlugin
import wiki.ecohub.provider.ExternalProvider

interface EnchantRegistrationIntegration : ExternalProvider {
    fun registerEnchants()

    override fun onLoad(plugin: BaseEcoPlugin) {
        EnchantRegistrations.register(this)
    }

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
