package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.PluginCommand
import org.bukkit.command.CommandSender

class CommandEcoEnchants(plugin: EcoPlugin) : PluginCommand(plugin, "ecoenchants", "ecoenchants.command.ecoenchants", false) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }

    init {
        addSubcommand(CommandReload(plugin))
            .addSubcommand(CommandToggleDescriptions(plugin))
            .addSubcommand(CommandGiveRandomBook(plugin))
    }
}