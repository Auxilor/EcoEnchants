package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.eco.core.items.builder.EnchantedBookBuilder
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.ecoenchants.enchants.wrap
import com.willfp.ecoenchants.rarity.EnchantmentRarities
import com.willfp.ecoenchants.rarity.EnchantmentRarity
import com.willfp.ecoenchants.type.EnchantmentType
import com.willfp.ecoenchants.type.EnchantmentTypes
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class CommandGiveRandomBook(plugin: EcoPlugin) : PluginCommand(
    plugin, "giverandombook", "ecoenchants.command.giverandombook", false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        val player = (args.firstOrNull()?: run { sender
            .sendMessage(plugin.langYml.getMessage("requires-player")); return }).asPlayer()?: run {
            sender.sendMessage(plugin.langYml.getMessage("invalid-player"))
            return
        }

        val filter = args.getOrNull(1)?.asRarity()?: args.getOrNull(1)?.asType()
        val minLevel = args.getOrNull(2)?.toIntOrNull()?: 1
        val maxLevel = args.getOrNull(3)?.toIntOrNull()?: Int.MAX_VALUE

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
                } && it.maxLevel >= minLevel
            }
            .randomOrNull()?: run {
                sender.sendMessage(plugin.langYml.getMessage("no-enchantments-found"))
                return
            }

        val level = (minLevel..maxLevel.coerceAtMost(enchantment.maxLevel)).random()

        player.inventory.addItem(EnchantedBookBuilder()
            .addStoredEnchantment(enchantment, level)
            .build()).values.forEach {
                player.world.dropItem(player.location, it)
            }
        sender.sendMessage(plugin.langYml.getMessage("random-book-given")
            .replace("%playername%", player.name)
            .replace("%enchantment%", enchantment.wrap()
                .getFormattedName(level)))
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        return when (args.size) {
            1 -> Bukkit.getOnlinePlayers().map { it.name }
            2 -> (EnchantmentRarities.values().map { it.id } + EnchantmentTypes.values().map { it.id })
            3 -> (1..10).map { it.toString() }
            4 -> ((args[3].toIntOrNull()?: 1)..(args[3].toIntOrNull()?: 1)+10).map { it.toString() }
            else -> emptyList()
        }
    }

    private fun String.asPlayer(): Player? {
        return Bukkit.getPlayer(this)
    }

    private fun String.asRarity(): EnchantmentRarity? {
        return EnchantmentRarities.getByID(this.lowercase())
    }

    private fun String.asType(): EnchantmentType? {
        return EnchantmentTypes.getByID(this.lowercase())
    }
}
