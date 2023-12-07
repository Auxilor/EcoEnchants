package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.drops.DropQueue
import com.willfp.eco.core.items.builder.EnchantedBookBuilder
import com.willfp.eco.util.NumberUtils
import com.willfp.eco.util.StringUtils
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

class CommandGiveRandomBook(plugin: EcoPlugin) : PluginCommand(
    plugin, "giverandombook", "ecoenchants.command.giverandombook", false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val playerName = args.getOrNull(0)

        if (playerName == null) {
            sender.sendMessage(plugin.langYml.getMessage("requires-player"))
            return
        }

        val player = Bukkit.getPlayer(playerName)

        if (player == null) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return
        }

        val filterName = args.getOrNull(1)

        val filter = if (filterName != null) {
            EnchantmentTypes[filterName] ?: EnchantmentRarities[filterName]
        } else null

        val minLevel = args.getOrNull(2)?.toIntOrNull() ?: 1
        val maxLevel = args.getOrNull(3)?.toIntOrNull() ?: Int.MAX_VALUE

        if (minLevel > maxLevel) {
            sender.sendMessage(plugin.langYml.getMessage("invalid-levels"))
            return
        }

        val enchantment = EcoEnchants.values()
            .filter {
                when (filter) {
                    is EnchantmentRarity -> it.enchantmentRarity == filter
                    is EnchantmentType -> it.type == filter
                    else -> true
                } && it.maximumLevel >= minLevel
            }
            .randomOrNull() ?: run {
            sender.sendMessage(plugin.langYml.getMessage("no-enchantments-found"))
            return
        }

        val level = NumberUtils.randInt(minLevel, maxLevel.coerceAtMost(enchantment.maximumLevel))

        val item = EnchantedBookBuilder()
            .addStoredEnchantment(enchantment.enchantment, level)
            .build()

        DropQueue(player)
            .addItem(item)
            .forceTelekinesis()
            .push()

        sender.sendMessage(
            plugin.langYml.getMessage("gave-random-book", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace("%player%", player.name)
                .replace("%enchantment%", enchantment.getFormattedName(level))
        )
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        // OfTeN wrote this - it's cursed, and I am *not* going to try refactor this.
        return when (args.size) {
            1 -> Bukkit.getOnlinePlayers().map { it.name }
            2 -> (EnchantmentRarities.values().map { it.id } + EnchantmentTypes.values().map { it.id })
            3 -> (1..10).map { it.toString() }
            4 -> {
                val startLevel = args[3].toIntOrNull() ?: 1
                val endLevel = startLevel + 10
                (startLevel..endLevel).map { it.toString() }
            }
            else -> emptyList()
        }
    }
}
