package com.willfp.ecoenchants.type

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.Registrable
import com.willfp.ecoenchants.libreforge.TriggerEnchantType
import com.willfp.ecoenchants.mechanics.infiniteIfNegative
import com.willfp.libreforge.triggers.Triggers
import java.util.Objects

class EnchantmentType(
    private val plugin: EcoPlugin,
    internal val config: Config
): Registrable {
    val id = config.getString("id")
    val format = config.getString("format")
    val limit = config.getInt("limit").infiniteIfNegative()
    val highLevelBias = config.getDouble("high-level-bias").coerceAtMost(0.999)
    val noGrindstone = config.getBool("no-grindstone")

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is EnchantmentType) {
            return false
        }

        return other.id == this.id
    }

    override fun onRegister() {
        Triggers.register(TriggerEnchantType(plugin, this))
    }

    override fun getID(): String {
        return this.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun toString(): String {
        return "EnchantmentType{$id}"
    }
}
