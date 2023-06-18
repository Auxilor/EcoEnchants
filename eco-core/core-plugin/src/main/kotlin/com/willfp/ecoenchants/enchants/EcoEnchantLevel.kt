package com.willfp.ecoenchants.enchants

import com.willfp.eco.core.EcoPlugin
import com.willfp.libreforge.Holder
import com.willfp.libreforge.conditions.ConditionList
import com.willfp.libreforge.effects.EffectList
import java.util.Objects

class EcoEnchantLevel(
    val enchant: EcoEnchant,
    val level: Int,
    override val effects: EffectList,
    override val conditions: ConditionList,
    plugin: EcoPlugin
) : Holder {
    override val id = plugin.createNamespacedKey("${enchant.id}_$level")

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

data class FoundEcoEnchantLevel(
    val level: EcoEnchantLevel,
    val activeLevel: Int
): Holder {
    override val effects = level.effects
    override val conditions = level.conditions
    override val id = level.id
}
