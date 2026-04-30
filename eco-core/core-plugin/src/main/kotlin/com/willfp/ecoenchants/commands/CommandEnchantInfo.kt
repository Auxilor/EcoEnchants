package com.willfp.ecoenchants.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.EnchantGUI
import com.willfp.ecoenchants.plugin
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

object CommandEnchantInfo : PluginCommand(
    plugin,
    "enchantinfo",
    "ecoenchants.command.enchantinfo",
    true
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender as Player

        if (args.isEmpty()) {
            sender.sendMessage(this.plugin.langYml.getMessage("missing-enchant"))
            return
        }

        val nameBuilder = StringBuilder()

        args.forEach { arg -> nameBuilder.append(arg).append(" ") }
        var searchName = nameBuilder.toString()
        searchName = searchName.substring(0, searchName.length - 1)

        val enchantment = EcoEnchants.getByName(searchName)

        if (enchantment == null) {
            val message = plugin.langYml.getMessage("not-found").replace("%name%", searchName)
            sender.sendMessage(message)
            return
        }

        EnchantGUI.openInfoGUI(sender, enchantment)
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        val completions = mutableListOf<String>()

        @Suppress("DEPRECATION")
        val names = EcoEnchants.values().mapNotNull { org.bukkit.ChatColor.stripColor(it.getFormattedName(0)) }

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return names
        }

        StringUtil.copyPartialMatches(args.joinToString(" "), names, completions)

        if (args.size > 1) {
            val prefix = args.dropLast(1).joinToString(" ") + " "
            val trimmed = completions.mapNotNull { completion ->
                if (completion.startsWith(prefix)) {
                    completion.removePrefix(prefix)
                } else null
            }
            completions.clear()
            completions.addAll(trimmed)
        }

        completions.sort()
        return completions
    }
}
