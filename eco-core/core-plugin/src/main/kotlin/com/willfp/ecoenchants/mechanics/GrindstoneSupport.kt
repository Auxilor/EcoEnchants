package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.fast
import com.willfp.eco.core.gui.player
import com.willfp.ecoenchants.enchant.wrap
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import java.util.*
import kotlin.math.max

@Suppress("DEPRECATION")
class GrindstoneSupport(
    private val plugin: EcoPlugin
) : Listener {
    @EventHandler
    fun preGrindstone(event: InventoryClickEvent) {
        val inventory = event.view.topInventory as? GrindstoneInventory ?: return

        // Run everything later to await event completion
        plugin.scheduler.run {
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

        val item = inventory.result ?: return

        if (item.fast().getEnchants(true).isEmpty()) {
            return
        }

        // Force remove XP
        plugin.scheduler.runLater(1) {
            val loc = inventory.location

            val orbs = loc?.getNearbyEntities(3.0, 3.0, 3.0)
                ?: emptyList()

            for (orb in orbs.filterIsInstance<ExperienceOrb>()) {
                orb.remove()
            }
        }
    }
}
