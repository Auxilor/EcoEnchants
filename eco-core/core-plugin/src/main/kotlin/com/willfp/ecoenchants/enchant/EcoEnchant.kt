package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.enchant.CustomEnchantment
import com.willfp.eco.core.registry.KRegistrable
import com.willfp.eco.util.toComponent
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.display.getFormattedNameComponent
import com.willfp.libreforge.conditions.ConditionList
import com.willfp.libreforge.slot.SlotType
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

interface EcoEnchant : KRegistrable, EcoEnchantLike, CustomEnchantment {
    /**
     * The key.
     */
    override val enchantmentKey: NamespacedKey

    /**
     * If this enchantment conflicts with all other enchantments.
     */
    val conflictsWithEverything: Boolean

    /**
     * The conflicts.
     */
    val conflicts: Set<Enchantment>

    /**
     * The required enchantments.
     */
    val required: Set<Enchantment>

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
    override val isObtainableThroughEnchanting: Boolean

    /**
     * If the enchantment is tradeable.
     */
    override val isObtainableThroughTrading: Boolean

    /**
     * If the enchantment is discoverable via a given [type].
     */
    fun isObtainableThrough(type: DiscoveryType): Boolean

    /**
     * If the enchantment is discoverable via any method.
     */
    override val isObtainableThroughDiscovery: Boolean
        get() = DiscoveryType.entries.any { isObtainableThrough(it) }

    /**
     * If the enchantment is hidden from the enchant GUI.
     */
    val isHiddenFromGui: Boolean

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
    override fun conflictsWith(other: Enchantment): Boolean {
        if (this.conflictsWithDirectly(other)) {
            return true
        }

        other.ecoEnchant?.let {
            return it.conflictsWithDirectly(this.enchantment)
        }

        return false
    }

    override val registryDescription: Component
        get() = nameTranslationKey?.let { Component.translatable(it) } ?: getFormattedNameComponent(0)

    override val translationKey: String
        get() = nameTranslationKey ?: "ecoenchants:enchantment.$id"

    override fun displayName(level: Int): Component {
        return nameTranslationKey?.let { Component.translatable(it, getFormattedName(level)) }
            ?: getFormattedName(level).toComponent()
    }

    override fun canEnchantItem(item: ItemStack): Boolean {
        return canEnchantItem(item, emptyList())
    }

    /**
     * Get if all required enchantments are in [other].
     */
    fun hasRequiredEnchantments(other: Collection<Enchantment>): Boolean {
        return required.all { requiredEnchant ->
            other.any { otherEnchant -> otherEnchant.key == requiredEnchant.key }
        }
    }
}
