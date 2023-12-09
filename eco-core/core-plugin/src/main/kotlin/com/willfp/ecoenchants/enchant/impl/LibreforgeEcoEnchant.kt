package com.willfp.ecoenchants.enchant.impl

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.util.containsIgnoreCase
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.ecoenchants.enchant.MissingDependencyException
import com.willfp.libreforge.SilentViolationContext
import com.willfp.libreforge.effects.EffectList
import com.willfp.libreforge.effects.Effects
import org.bukkit.Bukkit

class LibreforgeEcoEnchant(
    id: String,
    _config: Config,
    plugin: EcoEnchantsPlugin
) : EcoEnchantBase(id, plugin, _config) {
    private val effects: EffectList

    override fun createLevel(level: Int): EcoEnchantLevel {
        return EcoEnchantLevel(this, level, effects, conditions, plugin)
    }

    init {
        val missingPlugins = mutableSetOf<String>()

        for (dependency in config.getStrings("dependencies")) {
            if (!Bukkit.getPluginManager().plugins.map { it.name }.containsIgnoreCase(dependency)) {
                missingPlugins += dependency
            }
        }

        if (missingPlugins.isNotEmpty()) {
            throw MissingDependencyException(missingPlugins)
        }

        // Compile here so MissingDependencyException is thrown before effects are compiled
        effects = Effects.compile(
            config.getSubsections("effects"),
            if (plugin.isLoaded) context.with("effects") else SilentViolationContext
        )
    }
}
