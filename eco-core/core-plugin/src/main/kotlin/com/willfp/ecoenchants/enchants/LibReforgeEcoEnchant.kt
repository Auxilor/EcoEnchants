package com.willfp.ecoenchants.enchants

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.libreforge.Holder
import com.willfp.libreforge.conditions.ConfiguredCondition
import com.willfp.libreforge.effects.ConfiguredEffect
import com.willfp.libreforge.effects.Effects
import java.util.*

class LibReforgeEcoEnchant(
    id: String,
    config: Config,
    plugin: EcoEnchantsPlugin
) : EcoEnchant(
    id,
    config,
    plugin
) {
    private val effects: Set<ConfiguredEffect>

    init {
        effects = config.getSubsections("effects").mapNotNull {
            Effects.compile(it, "Enchantment $id")
        }.toSet()
    }

    override fun createLevel(level: Int): EcoEnchantLevel =
        EcoEnchantLevel(this, level, effects, conditions)
}

class EcoEnchantLevel(
    parent: EcoEnchant,
    level: Int,
    override val effects: Set<ConfiguredEffect>,
    override val conditions: Set<ConfiguredCondition>
) : Holder {
    override val id = "${parent.id}_$level"

    override fun equals(other: Any?): Boolean {
        if (other !is EcoEnchantLevel) {
            return false
        }

        return this.id == other.id
    }

    override fun toString(): String {
        return id
    }

    override fun hashCode(): Int {
        return Objects.hash(this.id)
    }
}
