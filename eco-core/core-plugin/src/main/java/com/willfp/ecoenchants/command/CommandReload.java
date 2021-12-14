package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.StringUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandReload extends Subcommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandReload(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin, "reload", "ecoenchants.command.reload", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        sender.sendMessage(this.getPlugin().getLangYml().getMessage("reloaded", StringUtils.FormatOption.WITHOUT_PLACEHOLDERS)
                .replace("%time%", NumberUtils.format(this.getPlugin().reloadWithTime())));
    }
}
