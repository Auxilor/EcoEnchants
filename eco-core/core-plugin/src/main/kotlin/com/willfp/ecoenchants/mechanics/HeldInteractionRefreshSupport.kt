package com.willfp.ecoenchants.mechanics

import com.willfp.ecoenchants.enchant.EcoEnchantLevel
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.target.EnchantFinder.clearEnchantmentCache
import com.willfp.libreforge.forceRefreshHolders
import com.willfp.libreforge.providedActiveEffects
import com.willfp.libreforge.slot.SlotItemProvidedHolder
import com.willfp.libreforge.toDispatcher
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.EquipmentSlot
import java.util.function.Consumer

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

        event.player.refreshEnchantHoldersNow()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun handle(event: PlayerItemHeldEvent) {
        event.player.refreshEnchantHoldersLater()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun handle(event: PlayerSwapHandItemsEvent) {
        event.player.refreshEnchantHoldersLater()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun handle(event: PlayerDropItemEvent) {
        event.player.refreshEnchantHoldersLater()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun handle(event: InventoryClickEvent) {
        (event.whoClicked as? Player)?.refreshEnchantHoldersLater()
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun handle(event: InventoryDragEvent) {
        (event.whoClicked as? Player)?.refreshEnchantHoldersLater()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun handle(event: PlayerJoinEvent) {
        event.player.refreshEnchantHoldersLater()
    }

    private fun Player.refreshEnchantHoldersNow() {
        this.clearEnchantmentCache()
        this.toDispatcher().forceRefreshHolders()
    }

    private fun Player.refreshEnchantHoldersLater() {
        this.scheduler.run(plugin, Consumer {
            if (this.isOnline) {
                this.refreshEnchantHoldersNow()
            }
        }, null)
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
