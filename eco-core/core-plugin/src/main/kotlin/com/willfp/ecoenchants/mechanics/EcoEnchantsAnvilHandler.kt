package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.anvil.AnvilHandler
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.wrap
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import java.util.concurrent.ConcurrentHashMap

/**
 * eco only passes [canCombine] the level of the enchant being added, so the levels of allowed
 * enchants are kept here. An enchant is only in `existing` once it has been allowed in that merge,
 * so this always holds the right level for enchants the target doesn't have.
 */
class EcoEnchantsAnvilHandler : AnvilHandler {
    private val allowedLevels = ThreadLocal.withInitial { ConcurrentHashMap<Enchantment, Int>() }

    override fun canCombine(
        enchant: Enchantment,
        level: Int,
        target: ItemStack,
        existing: Set<Enchantment>
    ): Boolean {
        val levels = allowedLevels.get()
        val targetLevels = target.fast().getEnchants(true)
        val canCombine = enchant.wrap().canEnchantItem(
            target,
            existing.associateWith { targetLevels[it] ?: levels[it] ?: 1 }
        )

        if (canCombine) {
            levels[enchant] = level
        }

        return canCombine
    }

    override fun maxLevel(enchant: Enchantment): Int = enchant.maxLevel

    override fun isBlocked(left: ItemStack?, right: ItemStack?): Boolean {
        val permanenceCurse = EcoEnchants.getByID("permanence_curse") ?: return false
        val enchantment = permanenceCurse.enchantment
        val targetHasCurse = left != null && left.fast().getEnchants(true).containsKey(enchantment)
        val sacrificeHasCurse = right != null && right.fast().getEnchants(true).containsKey(enchantment)
        return targetHasCurse || sacrificeHasCurse
    }
}
