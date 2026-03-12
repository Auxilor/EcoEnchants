package com.willfp.ecoenchants.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecoenchants.plugin
import org.bukkit.command.CommandSender

object CommandEcoEnchants : PluginCommand(
    plugin,
    "ecoenchants",
    "ecoenchants.command.ecoenchants",
    false
) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }

    init {
        addSubcommand(CommandReload)
            .addSubcommand(CommandToggleDescriptions)
            .addSubcommand(CommandGiveRandomBook)
            .addSubcommand(CommandGUI)
    }
}
