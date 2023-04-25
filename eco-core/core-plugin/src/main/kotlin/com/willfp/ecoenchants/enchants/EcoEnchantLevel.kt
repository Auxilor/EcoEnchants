package com.willfp.ecoenchants.enchants

import com.willfp.eco.core.EcoPlugin
import com.willfp.libreforge.Holder
import com.willfp.libreforge.conditions.ConditionList
import com.willfp.libreforge.effects.EffectList
import java.util.Objects

class EcoEnchantLevel(
    parent: EcoEnchant,
    val level: Int,
    override val effects: EffectList,
    override val conditions: ConditionList,
    plugin: EcoPlugin
) : Holder {
    override val id = plugin.createNamespacedKey("${parent.id}_$level")

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
