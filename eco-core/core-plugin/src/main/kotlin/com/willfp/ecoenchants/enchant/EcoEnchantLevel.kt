package com.willfp.ecoenchants.enchant

import com.willfp.ecoenchants.plugin
import wiki.ecohub.rules.condition.ConditionList
import wiki.ecohub.rules.effect.EffectList
import wiki.ecohub.rules.holder.Holder
import java.util.Objects

class EcoEnchantLevel(
    val enchant: EcoEnchant,
    val level: Int,
    override val effects: EffectList,
    override val conditions: ConditionList
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
