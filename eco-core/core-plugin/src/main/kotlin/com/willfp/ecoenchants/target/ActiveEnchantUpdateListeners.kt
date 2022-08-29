package com.willfp.ecoenchants.target

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.events.ArmorChangeEvent
import com.willfp.ecoenchants.target.EnchantLookup.clearEnchantCache
import com.willfp.ecoenchants.target.EnchantmentTargets.isEnchantable
import com.willfp.libreforge.updateEffects
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent

@Suppress("UNUSED", "UNUSED_PARAMETER")
class ActiveEnchantUpdateListeners(private val plugin: EcoPlugin) : Listener {
    @EventHandler
    fun onItemPickup(event: EntityPickupItemEvent) {
        if (event.entity !is Player) {
            return
        }
        val player = event.entity as Player

        if (!event.item.itemStack.isEnchantable) {
            return
        }

        refreshPlayer(player)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        refresh()
    }

    @EventHandler
    fun onInventoryDrop(event: PlayerDropItemEvent) {
        if (!event.itemDrop.itemStack.isEnchantable) {
            return
        }

        refreshPlayer(event.player)
    }

    @EventHandler
    fun onChangeSlot(event: PlayerItemHeldEvent) {
        refreshPlayer(event.player)
        plugin.scheduler.run { refreshPlayer(event.player) }
    }

    @EventHandler
    fun onArmorChange(event: ArmorChangeEvent) {
        refreshPlayer(event.player)
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.whoClicked !is Player) {
            return
        }
        refreshPlayer(event.whoClicked as Player)
    }

    private fun refresh() {
        plugin.server.onlinePlayers.forEach { player: Player -> refreshPlayer(player) }
    }

    private fun refreshPlayer(player: Player) {
        player.clearEnchantCache()
        player.updateEffects()
    }
}
