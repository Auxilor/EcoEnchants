package com.willfp.ecoenchants.mechanics

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.StringUtils
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.ecoenchants.enchants.wrap
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.GrindstoneInventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.EnchantmentStorageMeta
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt

@Suppress("DEPRECATION")
class GrindstoneSupport(
    private val plugin: EcoPlugin
) : Listener {
    fun onGrindstone(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        if (player.openInventory.topInventory.type != InventoryType.GRINDSTONE) {
            return
        }

        if (event.slotType != InventoryType.SlotType.RESULT) {
            return
        }

        val inventory = player.openInventory.topInventory as GrindstoneInventory

        val topEnchants = inventory.getItem(0)?.fast()?.getEnchants(true) ?: emptyMap()
        val bottomEnchants = inventory.getItem(1)?.fast()?.getEnchants(true) ?: emptyMap()

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

        plugin.scheduler.run {
            val result = inventory.getItem(2)
            if (result == null || event.isCancelled) {
                return@run
            }

            val meta = result.itemMeta ?: return@run

            if (toKeep.isEmpty()) {
                return@run
            }

            if (meta is EnchantmentStorageMeta) {
                for ((enchant, level) in toKeep) {
                    meta.addStoredEnchant(enchant, level, true)
                }
            } else {
                for ((enchant, level) in toKeep) {
                    meta.addEnchant(enchant, level, true)
                }
            }

            result.itemMeta = meta
        }
    }
}
