package com.willfp.ecoenchants.command.commands;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandEcoreload extends AbstractCommand {
    /**
     * Instantiate a new /ecoreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandEcoreload(@NotNull final EcoPlugin plugin) {
        super(plugin, "ecoreload", "ecoenchants.reload", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        this.getPlugin().reload();
        sender.sendMessage(this.getPlugin().getLangYml().getMessage("reloaded"));
    }
}
