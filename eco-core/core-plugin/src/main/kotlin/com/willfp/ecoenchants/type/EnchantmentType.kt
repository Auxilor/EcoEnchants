package com.willfp.ecoenchants.type

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.ecoenchants.mechanics.infiniteIfNegative
import java.util.*

@Suppress("DEPRECATION")
class EnchantmentType(
    internal val config: Config
) {
    val id = config.getString("id")
    val format = config.getString("format")
    val limit = config.getInt("limit").infiniteIfNegative()
    val highLevelBias = config.getDouble("high-level-bias")
    val noGrindstone = config.getBool("no-grindstone")

    init {
        EnchantmentTypes.addNewType(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is EnchantmentType) {
            return false
        }

        return other.id == this.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return "EnchantmentType{$id}"
    }
}
