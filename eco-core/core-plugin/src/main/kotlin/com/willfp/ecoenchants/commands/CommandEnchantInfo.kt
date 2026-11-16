package com.willfp.ecoenchants.commands

import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EnchantGUI
import com.willfp.ecoenchants.enchant.GuiEnchantments
import org.bukkit.ChatColor
import org.bukkit.util.StringUtil
import wiki.ecohub.core.command.playerCommand

internal fun EcoEnchantsPlugin.enchantInfoCommand() = playerCommand(name = "enchantinfo", permission = "ecoenchants.command.enchantinfo") {
    executes {
        if (args.isEmpty()) {
            sender.sendMessage(plugin.langYml.getMessage("missing-enchant"))
            return@executes
        }

        val level = if (args.size > 1) args.last().toIntOrNull() else null
        val nameArgs = if (level != null) args.dropLast(1) else args
        val searchName = nameArgs.joinToString(" ")

        val enchantment = GuiEnchantments.getByName(searchName)

        if (enchantment == null || (enchantment.isHiddenFromGui && !sender.hasPermission("ecoenchants.seehidden"))) {
            val message = plugin.langYml.getMessage("not-found").replace("%name%", searchName)
            sender.sendMessage(message)
            return@executes
        }

        EnchantGUI.openInfoGUI(sender, enchantment, level ?: -1)
    }

    completes {
        val completions = mutableListOf<String>()

        val canSeeHidden = sender.hasPermission("ecoenchants.seehidden")
        @Suppress("DEPRECATION")
        val names = GuiEnchantments.values().filter { !it.isHiddenFromGui || canSeeHidden }.mapNotNull { ChatColor.stripColor(it.getFormattedName(0)) }

        // If all args except the last form a complete enchant name, suggest level numbers
        if (args.size > 1) {
            val namePrefix = args.dropLast(1).joinToString(" ")
            val matched = GuiEnchantments.getByName(namePrefix)
            if (matched != null && (!matched.isHiddenFromGui || canSeeHidden)) {
                val levels = (1..matched.maximumLevel).map { it.toString() }
                StringUtil.copyPartialMatches(args.last(), levels, completions)
                return@completes completions
            }
        }

        StringUtil.copyPartialMatches(args.joinToString(" "), names, completions)

        if (args.size > 1) {
            val prefix = args.dropLast(1).joinToString(" ") + " "
            val trimmed = completions.mapNotNull { completion ->
                if (completion.startsWith(prefix, ignoreCase = true)) {
                    completion.substring(prefix.length)
                } else null
            }
            completions.clear()
            completions.addAll(trimmed)
        }

        completions
    }
}
