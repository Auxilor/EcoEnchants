package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.registry.KRegistrable
import com.willfp.ecoenchants.target.EnchantmentTarget
import com.willfp.libreforge.conditions.ConditionList
import com.willfp.libreforge.slot.SlotType
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment

interface EcoEnchant : KRegistrable, EcoEnchantLike {
    /**
     * The key.
     */
    val enchantmentKey: NamespacedKey

    /**
     * If this enchantment conflicts with all other enchantments.
     */
    val conflictsWithEverything: Boolean

    /**
     * The conflicts.
     */
    val conflicts: Set<Enchantment>

    /**
     * The enchantment targets.
     */
    val targets: Set<EnchantmentTarget>

    /**
     * The enchantment slots.
     */
    val slots: Set<SlotType>
        get() = targets.map { it.slot }.toSet()

    /**
     * The conditions to use the enchantment.
     */
    val conditions: ConditionList

    /**
     * If the enchantment is enchantable.
     */
    val isObtainableThroughEnchanting: Boolean

    /**
     * If the enchantment is tradeable.
     */
    val isObtainableThroughTrading: Boolean

    /**
     * If the enchantment is discoverable.
     */
    val isObtainableThroughDiscovery: Boolean

    /**
     * Get a certain [level].
     */
    fun getLevel(level: Int): EcoEnchantLevel

    /**
     * Get if this enchantment conflicts with [other], only checking one way.
     */
    fun conflictsWithDirectly(other: Enchantment): Boolean

    /**
     * Get if this enchantment conflicts with [other].
     */
    fun conflictsWith(other: Enchantment): Boolean {
        if (this.conflictsWithDirectly(other)) {
            return true
        }

        if (other is EcoEnchant) {
            return other.conflictsWithDirectly(this.enchantment)
        }

        return false
    }
}
