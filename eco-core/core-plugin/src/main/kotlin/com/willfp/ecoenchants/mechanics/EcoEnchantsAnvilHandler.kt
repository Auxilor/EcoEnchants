package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.anvil.AnvilHandler
import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.wrap
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class EcoEnchantsAnvilHandler : AnvilHandler {
    override fun canCombine(
        enchant: Enchantment,
        level: Int,
        target: ItemStack,
        existing: Set<Enchantment>
    ): Boolean = enchant.wrap().canEnchantItem(target, existing)

    override fun maxLevel(enchant: Enchantment): Int = enchant.maxLevel

    override fun isBlocked(left: ItemStack?, right: ItemStack?): Boolean {
        val permanenceCurse = EcoEnchants.getByID("permanence_curse") ?: return false
        val enchantment = permanenceCurse.enchantment
        val leftHas = left != null && left.fast().getEnchants(true).containsKey(enchantment)
        val rightHas = right != null && right.fast().getEnchants(true).containsKey(enchantment)
        return leftHas || rightHas
    }
}
