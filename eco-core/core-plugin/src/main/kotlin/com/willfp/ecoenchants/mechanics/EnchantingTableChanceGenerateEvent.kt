package com.willfp.ecoenchants.mechanics

import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import org.bukkit.inventory.ItemStack

class EnchantingTableChanceGenerateEvent(
    who: Player,
    val item: ItemStack,
    val enchantment: Enchantment,
    var chance: Double
) : PlayerEvent(who) {
    override fun getHandlers() = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList() = HANDLERS
    }
}
