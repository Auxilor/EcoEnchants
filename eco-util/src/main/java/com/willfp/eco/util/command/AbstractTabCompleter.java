package com.willfp.eco.util.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractTabCompleter implements TabCompleter {
    private final AbstractCommand command;

    protected AbstractTabCompleter(AbstractCommand command) {
        this.command = command;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase(this.command.getName()))
            return null;

        if (!sender.hasPermission(this.command.getPermission()))
            return null;

        return onTab(sender, Arrays.asList(args));
    }

    public abstract List<String> onTab(CommandSender sender, List<String> args);
}
