package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.command.impl.Subcommand
import org.bukkit.command.CommandSender

class CommandReload(plugin: EcoPlugin) : Subcommand(plugin, "reload", "ecoenchants.command.reload", false) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        plugin.reload()
        sender.sendMessage(plugin.langYml.getMessage("reload"))
    }
}
