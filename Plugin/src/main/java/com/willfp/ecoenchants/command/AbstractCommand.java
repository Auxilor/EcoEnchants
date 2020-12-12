package com.willfp.ecoenchants.command;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.util.interfaces.Registerable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, Registerable {
    private final String name;
    private final String permission;
    private final boolean playersOnly;

    protected AbstractCommand(String name, String permission, boolean playersOnly) {
        this.name = name;
        this.permission = permission;
        this.playersOnly = playersOnly;
    }

    public AbstractTabCompleter getTab() {
        return null;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (!command.getName().equalsIgnoreCase(name)) return false;

        if (playersOnly && !(sender instanceof Player)) {
            sender.sendMessage(ConfigManager.getLang().getMessage("not-player"));
            return true;
        }

        if (!sender.hasPermission(permission) && sender instanceof Player) {
            sender.sendMessage(ConfigManager.getLang().getNoPermission());
            return true;
        }

        onExecute(sender, Arrays.asList(args));

        return true;
    }

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

    public abstract void onExecute(CommandSender sender, List<String> args);
}
