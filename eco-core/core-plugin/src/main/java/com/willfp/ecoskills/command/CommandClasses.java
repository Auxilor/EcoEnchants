package com.willfp.ecoskills.command;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.AbstractCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandClasses extends AbstractCommand {
    /**
     * Instantiate a new /ecodebug command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandClasses(@NotNull final EcoPlugin plugin) {
        super(plugin, "classes", "ecoskills.classes", true);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {

    }
}
