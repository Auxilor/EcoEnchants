package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandLocale extends Subcommand {
    /**
     * Instantiate a new /ecoenchants locale command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandLocale(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin, "locale", "ecoenchants.command.locale", false);

        this.addSubcommand(new CommandLocaleExport(plugin))
                .addSubcommand(new CommandLocaleDownload(plugin));
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("specify-locale-subcommand"));
        };
    }
}
