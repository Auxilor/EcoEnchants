package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.plugin
import org.bukkit.ChatColor
import org.bukkit.NamespacedKey
import org.bukkit.Registry

object GuiEnchantments {
    @Volatile
    private var vanillaEnchants: List<EcoEnchantLike> = emptyList()

    @Volatile
    private var vanillaByName: Map<String, EcoEnchantLike> = emptyMap()

    private val showVanilla: Boolean
        get() = plugin.configYml.getBoolOrNull("enchant-gui.show-vanilla") != false

    internal fun reload() {
        @Suppress("DEPRECATION")
        vanillaEnchants = Registry.ENCHANTMENT
            .filter { enchantment ->
                enchantment.ecoEnchant == null &&
                    EcoEnchants.getByID(enchantment.key.key) == null &&
                    isListedVanilla(enchantment.key, plugin.vanillaEnchantsYml)
            }
            .map { it.wrap() }

        @Suppress("DEPRECATION")
        vanillaByName = vanillaEnchants.associateBy {
            ChatColor.stripColor(it.getFormattedName(0))?.lowercase().orEmpty()
        }
    }

    fun values(): List<EcoEnchantLike> =
        if (showVanilla) EcoEnchants.values().toList() + vanillaEnchants else EcoEnchants.values().toList()

    fun getByName(name: String): EcoEnchantLike? =
        resolveByName(name, EcoEnchants::getByName, vanillaByName, showVanilla)
}

internal fun isListedVanilla(key: NamespacedKey, vanillaEnchantsYml: Config): Boolean =
    key.namespace == NamespacedKey.MINECRAFT && vanillaEnchantsYml.has(key.key)

internal fun <T> resolveByName(
    name: String,
    custom: (String) -> T?,
    vanilla: Map<String, T>,
    showVanilla: Boolean
): T? =
    custom(name) ?: if (showVanilla) vanilla[name.lowercase()] else null
