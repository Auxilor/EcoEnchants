package com.willfp.ecoenchants.mechanics

import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.libreforge.forceRefreshHolders
import com.willfp.libreforge.providedActiveEffects
import com.willfp.libreforge.slot.SlotItemProvidedHolder
import com.willfp.libreforge.toDispatcher
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

object HeldInteractionRefreshSupport : Listener {
    private val interactionTriggers = setOf("alt_click", "click_block")

    @EventHandler(priority = EventPriority.LOWEST)
    fun handle(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) {
            return
        }

        if (event.action == Action.PHYSICAL) {
            return
        }

        val dispatcher = event.player.toDispatcher()

        if (!dispatcher.hasCachedMainHandInteractionEnchant()) {
            return
        }

        dispatcher.forceRefreshHolders()
    }

    private fun com.willfp.libreforge.Dispatcher<*>.hasCachedMainHandInteractionEnchant(): Boolean =
        this.providedActiveEffects.any { provided ->
            if (provided.holder.holder !is EcoEnchantLevel) {
                return@any false
            }

            val slotHolder = provided.holder as? SlotItemProvidedHolder<*> ?: return@any false

            slotHolder.slotType.id == "mainhand" &&
                    provided.effect.triggers.any { it.id in interactionTriggers }
        }
}
