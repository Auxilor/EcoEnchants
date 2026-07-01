package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.fast.fast
import com.willfp.ecoenchants.enchant.wrap
import com.willfp.ecoenchants.plugin
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.PrepareGrindstoneEvent
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import kotlin.math.max

@Suppress("DEPRECATION")
object GrindstoneSupport : Listener {
    @EventHandler(priority = EventPriority.HIGH)
    fun prepareGrindstone(event: PrepareGrindstoneEvent) {
        val inventory = event.inventory
        val result = event.result ?: return
        val inputEnchants = inventory.getInputEnchants()

        if (inputEnchants.isEmpty()) {
            return
        }

        event.result = result.withGrindstoneResultEnchants(inputEnchants.getNoGrindstoneEnchants())
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun postGrindstone(event: InventoryClickEvent) {
        val inventory = event.clickedInventory as? GrindstoneInventory ?: return

        if (event.slot != 2) {
            return
        }

        val item = inventory.result ?: return

        if (item.fast().getEnchants(true).keys.none { it.wrap().type.noGrindstone }) {
            return
        }

        for (delay in 1L..3L) {
            plugin.scheduler.runLater(delay) {
                val loc = inventory.location ?: return@runLater
                val orbs = loc.getNearbyEntitiesByType(ExperienceOrb::class.java, 3.0, 3.0, 3.0)
                    .filter { it.spawnReason == ExperienceOrb.SpawnReason.GRINDSTONE }
                for (orb in orbs) {
                    orb.remove()
                }
            }
        }
    }

    private fun GrindstoneInventory.getInputEnchants(): Map<Enchantment, Int> {
        val enchants = mutableMapOf<Enchantment, Int>()

        for (item in listOf(this.upperItem, this.lowerItem)) {
            for ((enchant, level) in item?.fast()?.getEnchants(true) ?: emptyMap()) {
                enchants[enchant] = max(enchants[enchant] ?: 0, level)
            }
        }

        return enchants
    }

    private fun Map<Enchantment, Int>.getNoGrindstoneEnchants(): Map<Enchantment, Int> =
        this.filterKeys { it.wrap().type.noGrindstone }

    private fun ItemStack.withGrindstoneResultEnchants(toKeep: Map<Enchantment, Int>): ItemStack {
        if (this.type == Material.ENCHANTED_BOOK && toKeep.isEmpty()) {
            return ItemStack(Material.BOOK, this.amount)
        }

        val result = this.clone()
        val meta = result.itemMeta ?: return result

        if (meta is EnchantmentStorageMeta) {
            for (enchant in meta.storedEnchants.keys.toSet()) {
                meta.removeStoredEnchant(enchant)
            }

            for ((enchant, level) in toKeep) {
                meta.addStoredEnchant(enchant, level, true)
            }
        } else {
            for (enchant in meta.enchants.keys.toSet()) {
                meta.removeEnchant(enchant)
            }

            for ((enchant, level) in toKeep) {
                meta.addEnchant(enchant, level, true)
            }
        }

        result.itemMeta = meta
        return result
    }
}
