package com.willfp.ecoenchants.target

import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.ecoenchants.enchant.ecoEnchant
import org.bukkit.inventory.ItemStack
import wiki.ecohub.game.item.fast
import wiki.ecohub.rules.holder.slot.ItemHolderFinder
import wiki.ecohub.rules.holder.slot.SlotType

object EnchantFinder : ItemHolderFinder<EcoEnchantLevel>() {
    override fun find(item: ItemStack): List<EcoEnchantLevel> {
        val enchantMap = item.fast().getEnchants()
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
