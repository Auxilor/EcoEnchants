package com.willfp.eco.util.command;

import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.interfaces.Registerable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * Wrapper for commands in {@link AbstractEcoPlugin}s
 * <p>
 * Reduces boilerplate and copying code between different commands.
 */
public abstract class AbstractCommand extends PluginDependent implements CommandExecutor, Registerable {
    /**
     * The name of the command
     * <p>
     * i.e. the name used on execution, for example /enchantinfo would have the name enchantinfo.
     */
    private final String name;

    /**
     * The permission required to execute the command.
     * <p>
     * Written out as a string for flexibility with subclasses.
     */
    private final String permission;

    /**
     * Should the command only be allowed to be executed by players?
     * <p>
     * In other worlds, only allowed to be executed by console.
     */
    private final boolean playersOnly;

    /**
     * Create a new command.
     * <p>
     * The command will not be registered until {@link this#register()} is called.
     * <p>
     * The name cannot be the same as an existing command as this will conflict.
     *
     * @param plugin      The owning {@link AbstractEcoPlugin}.
     * @param name        The name used in execution.
     * @param permission  The permission required to execute the command.
     * @param playersOnly If only players should be able to execute this command.
     */
    protected AbstractCommand(@NotNull final AbstractEcoPlugin plugin,
                              @NotNull final String name,
                              @NotNull final String permission,
                              final boolean playersOnly) {
        super(plugin);
        this.name = name;
        this.permission = permission;
        this.playersOnly = playersOnly;
    }

    /**
     * Get the {@link AbstractTabCompleter} associated with this command.
     * <p>
     * Implementations of {@link AbstractCommand} do not have to override this method:
     * null represents no associated tab-completer.
     *
     * @return The associated {@link AbstractTabCompleter}, or null if none associated.
     */
    public @Nullable AbstractTabCompleter getTab() {
        return null;
    }

    /**
     * Get the name of the permission required to execute this command.
     *
     * @return The name of the permission.
     */
    public String getPermission() {
        return this.permission;
    }

    /**
     * Get the name of the command used in execution.
     *
     * @return The command name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Internal implementation used to clean up boilerplate.
     * Used for parity with {@link CommandExecutor#onCommand(CommandSender, Command, String, String[])}.
     * <p>
     * Calls {@link this#onExecute(CommandSender, List)}.
     *
     * @param sender  The executor of the command.
     * @param command The bukkit command.
     * @param label   The name of the executed command.
     * @param args    The arguments of the command (anything after the physical command name)
     * @return If the command was processed by the linked {@link AbstractEcoPlugin}
     */
    @Override
    public final boolean onCommand(@NotNull final CommandSender sender,
                                   @NotNull final Command command,
                                   @NotNull final String label,
                                   @NotNull final String[] args) {
        if (!command.getName().equalsIgnoreCase(name)) {
            return false;
        }

        if (playersOnly && !(sender instanceof Player)) {
            sender.sendMessage(Configs.LANG.getMessage("not-player"));
            return true;
        }

        if (!sender.hasPermission(permission) && sender instanceof Player) {
            sender.sendMessage(Configs.LANG.getNoPermission());
            return true;
        }

        onExecute(sender, Arrays.asList(args));

        return true;
    }

    /**
     * Registers the command with the server,
     * <p>
     * Requires the command name to exist, defined in plugin.yml.
     */
    @Override
    public final void register() {
        PluginCommand command = Bukkit.getPluginCommand(name);
        assert command != null;
        command.setExecutor(this);

        AbstractTabCompleter tabCompleter = this.getTab();
        if (tabCompleter != null) {
            command.setTabCompleter(tabCompleter);
        }
    }

    /**
     * The code for the execution of the command (The actual functionality).
     * <p>
     * Unlike {@link this#onCommand(CommandSender, Command, String, String[])},
     * this does not return a value as the command <b>will</b> have been processed.
     *
     * @param sender The sender of the command
     * @param args   The arguments of the command
     */
    protected abstract void onExecute(CommandSender sender, List<String> args);
}
