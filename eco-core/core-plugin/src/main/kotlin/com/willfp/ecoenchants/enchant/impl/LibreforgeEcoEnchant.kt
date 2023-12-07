package com.willfp.ecoenchants.enchant.impl

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.libreforge.SilentViolationContext
import com.willfp.libreforge.effects.Effects

class LibreforgeEcoEnchant(
    id: String,
    private val _config: Config,
    plugin: EcoEnchantsPlugin
) : EcoEnchantBase(id, plugin) {
    private val effects = Effects.compile(
        config.getSubsections("effects"),
        if (plugin.isLoaded) context.with("effects") else SilentViolationContext
    )

    override fun loadConfig(): Config {
        return _config
    }

    override fun createLevel(level: Int): EcoEnchantLevel {
        return EcoEnchantLevel(this, level, effects, conditions, plugin)
    }
}
