package com.willfp.ecoenchants.integrations.plugins

import com.earth2me.essentials.Enchantments
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.integrations.EnchantRegistrationIntegration
import org.bukkit.enchantments.Enchantment

@Suppress("UNCHECKED_CAST")
object EssentialsIntegration : EnchantRegistrationIntegration {
    private val enchantmentMaps by lazy {
        arrayOf("ENCHANTMENTS", "ALIASENCHANTMENTS").map { field ->
            Enchantments::class.java.getDeclaredField(field)
                .apply { isAccessible = true }
                .get(null) as MutableMap<String, Enchantment>
        }
    }

    override fun registerEnchants() {
        for (enchantment in EcoEnchants.values()) {
            // why aren't you using the api you PRd in
            // because essentials named mending to repairing etc
            for (map in enchantmentMaps) {
                map[enchantment.id] = enchantment.enchantment
                map[enchantment.id.replace("_", "")] = enchantment.enchantment
            }
        }
    }

    override fun removeEnchant(enchantment: EcoEnchant) {
        for (map in enchantmentMaps) {
            map.remove(enchantment.id)
            map.remove(enchantment.id.replace("_", ""))
        }
    }

    override fun getPluginName() = "Essentials"
}
