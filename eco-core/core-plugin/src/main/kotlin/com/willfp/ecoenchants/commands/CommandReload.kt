package com.willfp.ecoenchants.commands

import com.willfp.ecoenchants.enchant.EcoEnchants
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import wiki.ecohub.core.command.CommandBuilder
import wiki.ecohub.core.platform.runGlobal
import wiki.ecohub.core.stdlib.toNiceString
import wiki.ecohub.ui.text.StringUtils

internal fun CommandBuilder<CommandSender>.reloadCommand() = subcommand(name = "reload", permission = "ecoenchants.command.reload") {
    executes {
        // On Folia, players run commands on their own region; reloading rebuilds shared
        // registries, so it belongs on the global region.
        Bukkit.getServer().runGlobal {
            sender.sendMessage(
                plugin.langYml.getMessage("reloaded", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                    .replace("%time%", plugin.reloadWithTime().toNiceString())
                    .replace("%count%", EcoEnchants.values().size.toString())
            )
        }
    }
}
