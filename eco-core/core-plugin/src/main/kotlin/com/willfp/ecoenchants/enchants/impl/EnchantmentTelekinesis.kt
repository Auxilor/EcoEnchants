package com.willfp.ecoenchants.enchants.impl

import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.events.EntityDeathByEntityEvent
import com.willfp.eco.core.integrations.antigrief.AntigriefManager
import com.willfp.eco.util.TelekinesisUtils
import com.willfp.eco.util.tryAsPlayer
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchant
import com.willfp.ecoenchants.target.EnchantLookup.hasEnchant
import com.willfp.ecoenchants.target.EnchantLookup.hasEnchantActive
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDropItemEvent

class EnchantmentTelekinesis(
    plugin: EcoEnchantsPlugin
) : EcoEnchant(
    "telekinesis",
    plugin,
    force = false
) {
    override fun onInit() {
        this.registerListener(TelekinesisHandler(this))
        TelekinesisUtils.registerTest { it.hasEnchantActive(this) }
    }

    private class TelekinesisHandler(
        private val enchant: EcoEnchant
    ) : Listener {
        @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
        )
        fun handle(event: BlockDropItemEvent) {
            val player = event.player
            val block = event.block

            if (!AntigriefManager.canBreakBlock(player, block)) {
                return
            }

            val drops = event.items.map { it.itemStack }
            event.items.clear()

            DropQueue(player)
                .setLocation(block.location)
                .addItems(drops)
                .push()
        }

        @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
        )
        fun handle(event: BlockBreakEvent) {
            val player = event.player
            val block = event.block

            if (!player.hasEnchantActive(enchant)) {
                return
            }

            if (!AntigriefManager.canBreakBlock(player, block)) {
                return
            }

            if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) {
                return
            }

            // Filter out telekinesis spawner xp to prevent dupe
            if (block.type == Material.SPAWNER && player.hasEnchant(enchant)) {
                event.expToDrop = 0
            }

            DropQueue(player)
                .setLocation(block.location)
                .addXP(event.expToDrop)
                .push()

            event.expToDrop = 0
        }


        @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
        )
        fun handle(event: EntityDeathByEntityEvent) {
            val victim = event.victim

            if (victim is Player && enchant.config.getBool("not-on-players")) {
                return
            }

            val player = event.killer.tryAsPlayer() ?: return

            // Only DropQueue-ify entity drops with telekinesis
            if (!player.hasEnchantActive(enchant)) {
                return
            }

            val xp = event.xp
            val drops = event.drops

            drops.removeAll { it == null }

            DropQueue(player)
                .addItems(drops)
                .setLocation(victim.location)
                .addXP(xp)
                .forceTelekinesis()
                .push()

            event.deathEvent.droppedExp = 0
            event.deathEvent.drops.clear()
        }
    }
}
