package com.willfp.ecoenchants.integrations.plugins

import com.earth2me.essentials.Enchantments
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.ecoenchants.integrations.EnchantRegistrationIntegration
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

class EssentialsIntegration: EnchantRegistrationIntegration {
    override fun registerEnchants() {
        for (enchantment in EcoEnchants.values()) {
            Enchantments.registerEnchantment(enchantment.id, enchantment)
        }
    }

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun removeEnchant(enchantment: EcoEnchant) {
        Enchantment::class.java.getDeclaredField("ENCHANTMENTS")
            .apply {
                isAccessible = true
                (get(null) as MutableMap<NamespacedKey, Enchantment>).apply {
                    for (enchant in values.filterIsInstance<EcoEnchant>()) {
                        remove(enchant.key)
                    }
                }
            }
    }

    override fun getPluginName() = "Essentials"
}
