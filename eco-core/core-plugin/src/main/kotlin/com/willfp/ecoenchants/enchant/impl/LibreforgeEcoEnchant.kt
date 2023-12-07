package com.willfp.ecoenchants.enchant.impl

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.libreforge.BlankHolder.conditions
import com.willfp.libreforge.BlankHolder.effects
import com.willfp.libreforge.SilentViolationContext
import com.willfp.libreforge.effects.Effects

class LibreforgeEcoEnchant(
    id: String,
    _config: Config,
    plugin: EcoEnchantsPlugin
) : EcoEnchantBase(id, plugin, _config) {
    private val effects = Effects.compile(
        config.getSubsections("effects"),
        if (plugin.isLoaded) context.with("effects") else SilentViolationContext
    )

    override fun createLevel(level: Int): EcoEnchantLevel {
        return EcoEnchantLevel(this, level, effects, conditions, plugin)
    }
}
