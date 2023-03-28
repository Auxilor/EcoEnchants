package com.willfp.ecoenchants.rarity

import com.willfp.eco.core.config.interfaces.Config
import com.willfp.eco.core.registry.Registrable
import java.util.*

@Suppress("DEPRECATION")
class EnchantmentRarity(
    internal val config: Config
) : Registrable {
    val id = config.getString("id")
    val displayName = config.getFormattedString("display-name")
    val tableChance = config.getDouble("table-chance")
    val minimumLevel = config.getInt("minimum-level")
    val villagerChance = config.getDouble("villager-chance")
    val lootChance = config.getDouble("loot-chance")

    override fun getID(): String {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other !is EnchantmentRarity) {
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
