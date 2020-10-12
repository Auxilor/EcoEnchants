package com.willfp.ecoenchants.command;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.util.interfaces.Registerable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, Registerable {
    private final String name;
    private final String permission;
    private final boolean playersOnly;
    private AbstractTabCompleter tabCompleter = null;

    protected AbstractCommand(String name, String permission, boolean playersOnly) {
        this.name = name;
        this.permission = permission;
        this.playersOnly = playersOnly;
    }

    public AbstractCommand setTab(AbstractTabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
        return this;
    }

    public String getPermission() {
        return this.permission;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equalsIgnoreCase(name)) return false;

        if(playersOnly && !(sender instanceof Player)) {
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
        Bukkit.getPluginCommand(name).setExecutor(this);
        if(tabCompleter != null) {
            Bukkit.getPluginCommand(name).setTabCompleter(tabCompleter);
        }
    }

    public abstract void onExecute(CommandSender sender, List<String> args);
}
