package com.willfp.ecoenchants.commands

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.command.impl.Subcommand
import com.willfp.ecoenchants.enchant.EnchantGUI
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandGUI(plugin: EcoPlugin) : Subcommand(plugin, "gui", "ecoenchants.command.gui", true) {
    override fun onExecute(player: CommandSender, args: List<String>) {
        player as Player
        EnchantGUI.openGUI(player)
    }
}
