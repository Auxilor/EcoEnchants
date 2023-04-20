package com.willfp.ecoenchants.enchants

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.libreforge.SilentViolationContext
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.effects.emptyEffectList

class LibReforgeEcoEnchant(
    id: String,
    config: Config,
    plugin: EcoEnchantsPlugin
) : EcoEnchant(
    id,
    config,
    plugin
) {
    private val effects = Effects.compile(
        config.getSubsections("effects"),
        if (plugin.isLoaded) ViolationContext(plugin, "Enchantment $id")
        else SilentViolationContext
    )

    override fun createLevel(level: Int): EcoEnchantLevel =
        EcoEnchantLevel(this, level, effects, conditions, plugin)
}
