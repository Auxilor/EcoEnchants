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
    val key: NamespacedKey

    /**
     * The enchantment.
     */
    override val enchantment: Enchantment
        get() = this as Enchantment

    /**
     * The max enchantment level.
     */
    val maxLevel: Int

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
    val isEnchantable: Boolean

    /**
     * If the enchantment is tradeable.
     */
    val isTradeable: Boolean

    /**
     * If the enchantment is discoverable.
     */
    val isDiscoverable: Boolean

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
            return other.conflictsWithDirectly(this as Enchantment)
        }

        return false
    }
}
