package com.willfp.ecoskills.command;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandEsreload extends AbstractCommand {
    /**
     * Instantiate a new /esreload command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandEsreload(@NotNull final EcoPlugin plugin) {
        super(plugin, "esreload", "ecoskills.reload", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        this.getPlugin().reload();
        sender.sendMessage(this.getPlugin().getLangYml().getMessage("reloaded"));
    }
}
