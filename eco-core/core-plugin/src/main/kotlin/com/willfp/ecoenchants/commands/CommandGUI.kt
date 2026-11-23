package com.willfp.ecoenchants.commands

import com.willfp.ecoenchants.enchant.EnchantGUI
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder

internal fun CommandBuilder<CommandSender>.guiCommand() = playerSubcommand(name = "gui", permission = "ecoenchants.command.gui") {
    executes {
        EnchantGUI.openGUI(sender)
    }
}
