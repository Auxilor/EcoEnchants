package com.willfp.ecoenchants.enchants

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
    plugin: EcoEnchantsPlugin
) : EcoEnchant(
    id,
    config,
    plugin
) {
    private val effects: EffectList

    init {
        effects = if (plugin.isLoaded) Effects.compile(
            config.getSubsections("effects"),
            ViolationContext(plugin, "Enchantment $id")
        ) else emptyEffectList()
    }

    override fun createLevel(level: Int): EcoEnchantLevel =
        EcoEnchantLevel(this, level, effects, conditions, plugin)
}

class EcoEnchantLevel(
    parent: EcoEnchant,
    level: Int,
    override val effects: EffectList,
    override val conditions: ConditionList,
    plugin: LibreforgePlugin
) : Holder {
    override val id =  plugin.createNamespacedKey("${parent.id}_$level")

    override fun equals(other: Any?): Boolean {
        if (other !is EcoEnchantLevel) {
            return false
        }

        return this.id == other.id
    }

    override fun toString(): String {
        return id.toString()
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }
}
