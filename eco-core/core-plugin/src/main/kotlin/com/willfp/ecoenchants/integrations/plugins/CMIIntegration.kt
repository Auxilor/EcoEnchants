package com.willfp.ecoenchants.integrations.plugins

import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchant
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.integrations.EnchantRegistrationIntegration
import com.willfp.ecoenchants.stripLegacyFormatting
import net.Zrips.CMILib.Enchants.CMIEnchantment
import org.bukkit.enchantments.Enchantment

@Suppress("UNCHECKED_CAST")
object CMIIntegration: EnchantRegistrationIntegration {
    private val byName by lazy {
        CMIEnchantment::class.java.getDeclaredField("byName")
            .apply { isAccessible = true }
            .get(null) as MutableMap<String, Enchantment>
    }

    private val enchantList by lazy {
        CMIEnchantment::class.java.getDeclaredField("enchantList")
            .apply { isAccessible = true }
            .get(null) as MutableMap<String, String>
    }

    private val translatedEnchantList by lazy {
        CMIEnchantment::class.java.getDeclaredField("transaltedEnchantList")
            .apply { isAccessible = true }
            .get(null) as MutableMap<String, String>
    }

    override fun registerEnchants() {
        CMIEnchantment.initialize()
        CMIEnchantment.saveEnchants()

        for (enchantment in EcoEnchants.values()) {
            registerEnchant(enchantment)
        }
    }

    override fun removeEnchant(enchantment: EcoEnchant) {
        val aliases = enchantment.aliases().map { it.normalize() }.toSet()
        val canonical = enchantment.canonicalName()

        for (alias in aliases) {
            byName.remove(alias)
            enchantList.remove(alias)
            translatedEnchantList.remove(alias)
        }

        byName.entries.removeIf { it.value == enchantment.enchantment }
        enchantList.entries.removeIf { it.value == canonical }
        translatedEnchantList.remove(canonical)
    }

    override fun getPluginName() = "CMI"

    private fun registerEnchant(enchantment: EcoEnchant) {
        val canonical = enchantment.canonicalName()
        val displayName = enchantment.getFormattedName(0).stripLegacyFormatting()

        byName[canonical] = enchantment.enchantment
        translatedEnchantList[canonical] = displayName

        for (alias in enchantment.aliases()) {
            val normalized = alias.normalize()
            if (normalized.isBlank()) {
                continue
            }

            byName[normalized] = enchantment.enchantment
            enchantList[normalized] = canonical
        }
    }

    private fun EcoEnchant.canonicalName(): String =
        this.id.normalize()

    private fun EcoEnchant.aliases(): Set<String> {
        val displayName = this.getFormattedName(0).stripLegacyFormatting()

        return setOf(
            this.id,
            this.enchantment.key.key,
            displayName
        )
    }

    private fun String.normalize(): String =
        this.replace("_", "")
            .replace(" ", "")
            .lowercase()
}
