package com.willfp.eco.util.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractTabCompleter implements TabCompleter {
    /**
     * The {@link AbstractCommand} that is tab-completed.
     */
    private final AbstractCommand command;

    /**
     * Create a tab-completer for a specified {@link AbstractCommand}.
     *
     * @param command The command to tab-complete.
     */
    protected AbstractTabCompleter(@NotNull final AbstractCommand command) {
        this.command = command;
    }

    /**
     * Internal implementation used to clean up boilerplate.
     * Used for parity with {@link TabCompleter#onTabComplete(CommandSender, Command, String, String[])}.
     * <p>
     * Calls {@link this#onTab(CommandSender, List)}.
     *
     * @param sender  The executor of the command.
     * @param command The bukkit command.
     * @param label   The name of the executed command.
     * @param args    The arguments of the command (anything after the physical command name).
     * @return The list of tab-completions.
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull final CommandSender sender,
                                                @NotNull final Command command,
                                                @NotNull final String label,
                                                @NotNull final String[] args) {
        if (!command.getName().equalsIgnoreCase(this.command.getName())) {
            return null;
        }

        if (!sender.hasPermission(this.command.getPermission())) {
            return null;
        }

        return onTab(sender, Arrays.asList(args));
    }

    /**
     * The code for the tab-completion of the command (The actual functionality).
     * <p>
     *
     * @param sender The sender of the command.
     * @param args   The arguments of the command.
     * @return The list of tab-completions.
     */
    public abstract List<String> onTab(@NotNull CommandSender sender,
                                       @NotNull List<String> args);
}
