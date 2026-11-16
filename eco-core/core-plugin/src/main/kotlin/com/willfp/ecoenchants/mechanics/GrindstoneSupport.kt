package com.willfp.ecoenchants.mechanics

import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import wiki.ecohub.core.plugin.startup.Prerequisite
import wiki.ecohub.game.item.fast
import kotlin.math.max

@Suppress("DEPRECATION")
object GrindstoneSupport : Listener {
    @EventHandler
    fun preGrindstone(event: InventoryClickEvent) {
        val inventory = event.view.topInventory as? GrindstoneInventory ?: return

        // Run everything later to await event completion
        plugin.scheduler.on(event.whoClicked).run {
            val topEnchants = inventory.getItem(0)?.fast()?.getEnchants(true) ?: emptyMap()
            val bottomEnchants = inventory.getItem(1)?.fast()?.getEnchants(true) ?: emptyMap()

            if (topEnchants.isEmpty() && bottomEnchants.isEmpty()) {
                return@run
            }

            val toKeep = mutableMapOf<Enchantment, Int>()

            for ((enchant, level) in topEnchants) {
                if (enchant.wrap().type.noGrindstone) {
                    toKeep[enchant] = level
                }
            }

            for ((enchant, level) in bottomEnchants) {
                if (enchant.wrap().type.noGrindstone) {
                    val current = toKeep[enchant] ?: 0
                    toKeep[enchant] = max(level, current)
                }
            }

            val result = inventory.getItem(2)

            if (result == null || event.isCancelled) {
                return@run
            }

            val meta = result.itemMeta ?: return@run

            if (toKeep.isEmpty()) {
                return@run
            }

            if (meta is EnchantmentStorageMeta) {
                for ((enchant, _) in meta.storedEnchants.toMap()) {
                    meta.removeStoredEnchant(enchant)
                }

                for ((enchant, level) in toKeep) {
                    meta.addStoredEnchant(enchant, level, true)
                }
            } else {
                for ((enchant, _) in meta.enchants.toMap()) {
                    meta.removeEnchant(enchant)
                }

                for ((enchant, level) in toKeep) {
                    meta.addEnchant(enchant, level, true)
                }
            }

            result.itemMeta = meta
        }
    }

    @EventHandler
    fun postGrindstone(event: InventoryClickEvent) {
        val inventory = event.clickedInventory as? GrindstoneInventory ?: return

        if (event.slot != 2) {
            return
        }

        val item = inventory.getItem(2) ?: return

        if (item.fast().getEnchants(true).isEmpty()) {
            return
        }

        val loc = inventory.location ?: return
        val world = loc.world ?: return

        // Force remove XP
        plugin.scheduler.at(loc).runLater(1) {
            val center = loc.clone().add(0.5, 0.5, 0.5)
            val orbs = world.getNearbyEntities(center, 0.5, 0.5, 0.5) { entity ->
                entity is ExperienceOrb && entity.ticksLived <= 2 && entity.isFromGrindstone()
            }
            for (orb in orbs) {
                orb.remove()
            }
        }
    }
}

// Spigot does not record why an orb spawned, so only the age filter applies there.
private fun ExperienceOrb.isFromGrindstone(): Boolean =
    !Prerequisite.HAS_PAPER.isMet || spawnReason == ExperienceOrb.SpawnReason.GRINDSTONE
