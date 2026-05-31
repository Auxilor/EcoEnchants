package com.willfp.ecoenchants.integrations.plugins

import com.earth2me.essentials.Enchantments
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.integrations.EnchantRegistrationIntegration
import com.willfp.ecoenchants.stripLegacyFormatting
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
                for (alias in enchantment.aliases()) {
                    map[alias] = enchantment.enchantment
                }
            }
        }
    }

    override fun removeEnchant(enchantment: EcoEnchant) {
        for (map in enchantmentMaps) {
            for (alias in enchantment.aliases()) {
                map.remove(alias)
            }
        }
    }

    override fun getPluginName() = "Essentials"

    private fun EcoEnchant.aliases(): Set<String> {
        val displayName = this.getFormattedName(0).stripLegacyFormatting()

        return setOf(
            this.id,
            this.id.replace("_", ""),
            this.enchantment.key.key,
            displayName,
            displayName.replace(" ", ""),
            displayName.replace("_", "")
        ).map { it.lowercase() }
            .filter { it.isNotBlank() }
            .toSet()
    }
}
