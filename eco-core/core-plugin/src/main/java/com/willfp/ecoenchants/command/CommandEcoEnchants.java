package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.PluginCommand;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandEcoEnchants extends PluginCommand {
    /**
     * Instantiate a new /ecoenchants command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandEcoEnchants(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin, "ecoenchants", "ecoenchants.command.ecoenchants", false);

        this.addSubcommand(new CommandDebug(plugin))
                .addSubcommand(new CommandReload(plugin))
                .addSubcommand(new CommandGiverandombook(plugin))
                .addSubcommand(new CommandRandomenchant(plugin));
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-command"));
        };
    }
}
