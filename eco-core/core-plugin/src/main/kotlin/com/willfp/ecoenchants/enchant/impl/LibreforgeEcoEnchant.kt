package com.willfp.ecoenchants.enchant.impl

import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.ecoenchants.enchant.MissingDependencyException
import com.willfp.ecoenchants.plugin
import org.bukkit.Bukkit
import wiki.ecohub.config.Config
import wiki.ecohub.config.SilentViolationContext
import wiki.ecohub.rules.effect.EffectList
import wiki.ecohub.rules.effect.Effects
import wiki.ecohub.ui.text.containsIgnoreCase

class LibreforgeEcoEnchant(
    id: String,
    _config: Config
) : EcoEnchantBase(id, plugin, _config) {
    private val effects: EffectList

    override fun createLevel(level: Int): EcoEnchantLevel {
        return EcoEnchantLevel(this, level, effects, conditions)
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
