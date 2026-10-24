package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchant.removeEnchantsReplacedBy
import com.willfp.ecoenchants.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory

/**
 * Removes enchantments replaced by newly applied enchantments from anvil results,
 * as the anvil merge itself is handled by eco.
 */
object AnvilRequirementSupport : Listener {
    /**
     * eco renders the result a tick after the event, so this runs on the next tick, right after it.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    fun onAnvilPrepare(event: PrepareAnvilEvent) {
        val inventory = event.inventory

        plugin.scheduler.run {
            removeReplacedFromResult(inventory)
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onAnvilResultClick(event: InventoryClickEvent) {
        val inventory = event.view.topInventory as? AnvilInventory ?: return

        if (event.rawSlot != 2) {
            return
        }

        removeReplacedFromResult(inventory)
    }

    private fun removeReplacedFromResult(inventory: AnvilInventory) {
        val left = inventory.getItem(0)
        val result = inventory.getItem(2)

        if (left == null || result == null || result.type.isAir) {
            return
        }

        val added = result.fast().getEnchants(true).keys - left.fast().getEnchants(true).keys

        if (result.removeEnchantsReplacedBy(added)) {
            inventory.setItem(2, result)
        }
    }
}
