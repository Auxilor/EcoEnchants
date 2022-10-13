package com.willfp.ecoenchants.commands

import com.willfp.eco.core.command.impl.PluginCommand
import com.willfp.ecoenchants.EcoEnchantsPlugin
import com.willfp.ecoenchants.enchants.EcoEnchants
import com.willfp.libreforge.lrcdb.CommandExport
import com.willfp.libreforge.lrcdb.CommandImport
import com.willfp.libreforge.lrcdb.ExportableConfig
import org.bukkit.command.CommandSender

class CommandEcoEnchants(plugin: EcoEnchantsPlugin) :
    PluginCommand(plugin, "ecoenchants", "ecoenchants.command.ecoenchants", false) {
    override fun onExecute(sender: CommandSender, args: List<String>) {
        sender.sendMessage(
            plugin.langYml.getMessage("invalid-command")
        )
    }

    init {
        addSubcommand(CommandReload(plugin))
            .addSubcommand(CommandToggleDescriptions(plugin))
            .addSubcommand(CommandGiveRandomBook(plugin))
            .addSubcommand(CommandGUI(plugin))
            .addSubcommand(CommandImport("enchants", plugin))
            .addSubcommand(CommandExport(plugin) {
                EcoEnchants.values().map {
                    ExportableConfig(
                        it.id,
                        it.config
                    )
                }
            })
    }
}
