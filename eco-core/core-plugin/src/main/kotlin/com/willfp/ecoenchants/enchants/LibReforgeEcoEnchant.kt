package com.willfp.ecoenchants.enchants

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.libreforge.Holder
import com.willfp.libreforge.ViolationContext
import com.willfp.libreforge.conditions.ConditionList
import com.willfp.libreforge.effects.EffectList
import com.willfp.libreforge.effects.Effects
import com.willfp.libreforge.effects.emptyEffectList
import com.willfp.libreforge.loader.LibreforgePlugin
import java.util.Objects

class LibReforgeEcoEnchant(
    id: String,
    config: Config,
    plugin: EcoPlugin
) : EcoEnchant(
    id,
    config,
    plugin
) {
    private val effects = Effects.compile(
        config.getSubsections("effects"),
        ViolationContext(plugin, "Enchantment $id")
    )

    override fun createLevel(level: Int): EcoEnchantLevel =
        EcoEnchantLevel(this, level, effects, conditions, plugin)
}
