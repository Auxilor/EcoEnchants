package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.Prerequisite
import com.willfp.eco.core.command.impl.Subcommand
import org.bukkit.command.CommandSender

class CommandReload(plugin: EcoPlugin) : Subcommand(plugin, "reload", "ecoenchants.command.reload", false) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        plugin.reload()
        if (Prerequisite.HAS_1_21.isMet) {
            sender.sendMessage(plugin.langYml.getMessage("reloaded-121"))
        } else if (Prerequisite.HAS_1_20_3.isMet) {
            sender.sendMessage(plugin.langYml.getMessage("reloaded-1203"))
        } else {
            sender.sendMessage(plugin.langYml.getMessage("reloaded"))
        }
    }
}
