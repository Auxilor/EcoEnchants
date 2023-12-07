package com.willfp.ecoenchants.enchant

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.fast.fast
import com.willfp.eco.util.NumberUtils
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.inventory.BlockInventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.EnchantmentStorageMeta

class LoreConversion(
    private val plugin: EcoPlugin
) : Listener {
    @EventHandler
    fun loreConverter(event: PlayerItemHeldEvent) {
        if (!plugin.configYml.getBool("lore-conversion.enabled")) {
            return
        }

        convertLore(event.player.inventory.getItem(event.newSlot))
    }

    @EventHandler
    fun aggressiveLoreConverter(event: InventoryOpenEvent) {
        if (!plugin.configYml.getBool("lore-conversion.enabled")) {
            return
        }
        if (!plugin.configYml.getBool("lore-conversion.aggressive")) {
            return
        }

        val inventory = event.inventory

        if (inventory.holder !is BlockInventoryHolder) {
            return
        }

        for (itemStack in inventory.contents) {
            convertLore(itemStack)
        }
    }

    private fun convertLore(itemStack: ItemStack?) {
        if (itemStack == null) {
            return
        }

        val meta = itemStack.itemMeta ?: return

        val toAdd = mutableMapOf<Enchantment, Int>()

        val lore = itemStack.fast().lore.toMutableList()

        for (line in lore.toList()) {
            val uncolored = ChatColor.stripColor(line) ?: continue

            var enchant: EcoEnchant?
            var level: Int
            val split = uncolored.split(" ").toMutableList()

            if (split.isEmpty()) {
                continue
            }

            if (split.size == 1) {
                enchant = EcoEnchants.getByName(split[0])
                level = 1
            } else {
                val attemptFullLine = EcoEnchants.getByName(line)
                if (attemptFullLine != null) {
                    enchant = attemptFullLine
                    level = 1
                } else {
                    var levelString = split.last()
                    split.remove(levelString)
                    levelString = levelString.trim { it <= ' ' }
                    level = try {
                        NumberUtils.fromNumeral(levelString)
                    } catch (e: IllegalArgumentException) {
                        continue
                    }
                    val enchantName = split.joinToString(" ")
                    enchant = EcoEnchants.getByName(enchantName)
                }
            }

            if (enchant == null) {
                continue
            }

            toAdd[enchant.enchantment] = level
        }


        if (meta is EnchantmentStorageMeta) {
            lore.clear()
            for ((enchant, level) in toAdd) {
                meta.addStoredEnchant(enchant, level, true)
            }
        } else {
            lore.clear()
            for ((enchant, level) in toAdd) {
                meta.addEnchant(enchant, level, true)
            }
        }

        itemStack.itemMeta = meta
        itemStack.fast().lore = lore
    }
}