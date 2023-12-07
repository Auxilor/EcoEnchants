package com.willfp.ecoenchants.integrations.plugins

import com.earth2me.essentials.Enchantments
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.integrations.EnchantRegistrationIntegration
import org.bukkit.enchantments.Enchantment

@Suppress("UNCHECKED_CAST")
class EssentialsIntegration : EnchantRegistrationIntegration {
    override fun registerEnchants() {
        for (enchantment in EcoEnchants.values()) {
            // why aren't you using the api you PRd in
            // because essentials named mending to repairing etc
            for (field in arrayOf("ENCHANTMENTS", "ALIASENCHANTMENTS")) {
                Enchantments::class.java.getDeclaredField(field)
                    .apply {
                        isAccessible = true
                        (get(null) as MutableMap<String, Enchantment>).apply {
                            put(enchantment.id, enchantment.enchantment)
                            put(enchantment.id.replace("_", ""), enchantment.enchantment)
                        }
                    }
            }
        }
    }

    override fun removeEnchant(enchantment: EcoEnchant) {
        for (field in arrayOf("ENCHANTMENTS", "ALIASENCHANTMENTS")) {
            Enchantments::class.java.getDeclaredField(field)
                .apply {
                    isAccessible = true
                    (get(null) as MutableMap<String, Enchantment>).apply {
                        remove(enchantment.id)
                        remove(enchantment.id.replace("_", ""))
                    }
                }
        }
    }

    override fun getPluginName() = "Essentials"
}
