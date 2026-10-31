package com.willfp.ecoenchants.target

import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.ecoenchants.enchant.ecoEnchant
import com.willfp.libreforge.slot.ItemHolderFinder
import com.willfp.libreforge.slot.SlotType
import org.bukkit.inventory.ItemStack

object EnchantFinder : ItemHolderFinder<EcoEnchantLevel>() {
    override fun find(item: ItemStack): List<EcoEnchantLevel> {
        val enchantMap = item.fast().enchants
        val enchants = mutableListOf<EcoEnchantLevel>()

        for ((enchant, level) in enchantMap) {
            enchants += enchant.ecoEnchant?.getLevel(level) ?: continue
        }

        return enchants
    }

    override fun isValidInSlot(holder: EcoEnchantLevel, slot: SlotType): Boolean {
        return holder.enchant.targets.map { it.slot }.any { it.isOrContains(slot) }
    }
}
