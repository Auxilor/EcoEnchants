package com.willfp.ecoenchants.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecoenchants.display.getFormattedName
import com.willfp.ecoenchants.enchant.EcoEnchants
import com.willfp.ecoenchants.enchant.EnchantGUI
import com.willfp.ecoenchants.plugin
import com.willfp.ecoenchants.stripLegacyFormatting
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

object CommandEnchantInfo : PluginCommand(
    plugin,
    "enchantinfo",
    "ecoenchants.command.enchantinfo",
    true
) {
    private var enchantmentCompletions: List<String> = emptyList()
    private var levelCompletionsByName = emptyMap<String, List<String>>()

    internal fun reload() {
        val namesWithEnchantments = EcoEnchants.values().map { enchantment ->
            enchantment.getFormattedName(0).stripLegacyFormatting() to enchantment
        }

        enchantmentCompletions = namesWithEnchantments.map { it.first }
        levelCompletionsByName = namesWithEnchantments.associate { (name, enchantment) ->
            name.lowercase() to (1..enchantment.maximumLevel).map { it.toString() }
        }
    }

    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender as Player

        if (args.isEmpty()) {
            sender.sendMessage(this.plugin.langYml.getMessage("missing-enchant"))
            return
        }

        val level = if (args.size > 1) args.last().toIntOrNull() else null
        val nameArgs = if (level != null) args.dropLast(1) else args
        val searchName = nameArgs.joinToString(" ")

        val enchantment = EcoEnchants.getByName(searchName)

        if (enchantment == null) {
            val message = plugin.langYml.getMessage("not-found").replace("%name%", searchName)
            sender.sendMessage(message)
            return
        }

        EnchantGUI.openInfoGUI(sender, enchantment, level ?: -1)
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        if (enchantmentCompletions.isEmpty()) {
            reload()
        }

        val completions = mutableListOf<String>()

        if (args.isEmpty()) {
            // Currently, this case is not ever reached
            return enchantmentCompletions
        }

        // If all args except the last form a complete enchant name, suggest level numbers
        if (args.size > 1) {
            val namePrefix = args.dropLast(1).joinToString(" ")
            val levels = levelCompletionsByName[namePrefix.lowercase()]
            if (levels != null) {
                StringUtil.copyPartialMatches(args.last(), levels, completions)
                return completions
            }
        }

        StringUtil.copyPartialMatches(args.joinToString(" "), enchantmentCompletions, completions)

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

        completions.sort()
        return completions
    }
}
