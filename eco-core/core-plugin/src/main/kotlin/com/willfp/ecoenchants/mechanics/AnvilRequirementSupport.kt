package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.removeEnchantsReplacedBy
import com.willfp.ecoenchants.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.AnvilInventory

/**
 * Removes enchantments replaced by newly applied enchantments from anvil results,
 * as the anvil merge itself is handled by eco.
 */
object AnvilRequirementSupport : Listener {
    /**
     * eco renders the result a tick after the event, so this runs on the next tick, right after it.
     *
     * This listener is registered after eco's at the same priority, and eco clears the result when
     * it takes over the anvil. A result that is still set belongs to another plugin, so it's left alone.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onAnvilPrepare(event: PrepareAnvilEvent) {
        val player = event.view.player

        if (event.result != null || plugin.isDisabledIn(player.world) || !anyEnchantReplaces()) {
            return
        }

        val inventory = event.inventory

        plugin.scheduler.on(player).run {
            removeReplacedFromResult(inventory)
        }
    }

    private fun anyEnchantReplaces(): Boolean =
        EcoEnchants.values().any { enchant -> enchant.requirements.any { it.remove } }

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
