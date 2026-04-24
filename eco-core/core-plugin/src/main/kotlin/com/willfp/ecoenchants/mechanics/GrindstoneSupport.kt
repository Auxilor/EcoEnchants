package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.gui.player
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import kotlin.math.max

@Suppress("DEPRECATION")
object GrindstoneSupport : Listener {
    @EventHandler
    fun preGrindstone(event: InventoryClickEvent) {
        val inventory = event.view.topInventory as? GrindstoneInventory ?: return

        // Run everything later to await event completion
        plugin.scheduler.runTask(event.player) {
            val topEnchants = inventory.getItem(0)?.fast()?.getEnchants(true) ?: emptyMap()
            val bottomEnchants = inventory.getItem(1)?.fast()?.getEnchants(true) ?: emptyMap()

            if (topEnchants.isEmpty() && bottomEnchants.isEmpty()) {
                return@runTask
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
                return@runTask
            }

            val meta = result.itemMeta ?: return@runTask

            if (toKeep.isEmpty()) {
                return@runTask
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

        val item = inventory.result ?: return

        if (item.fast().getEnchants(true).isEmpty()) {
            return
        }

        val loc = inventory.location ?: return

        // Force remove XP
        plugin.scheduler.runTaskLater(loc, 1) {
            val loc = inventory.location ?: return@runTaskLater
            val orbs = loc.getNearbyEntities(3.0, 3.0, 3.0)
                .filterIsInstance<ExperienceOrb>()
                .filter { it.spawnReason == ExperienceOrb.SpawnReason.GRINDSTONE }
            for (orb in orbs) {
                orb.remove()
            }
        }
    }
}
