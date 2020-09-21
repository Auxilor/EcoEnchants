package com.willfp.ecoenchants.command;

import com.willfp.ecoenchants.config.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand implements CommandExecutor {
    protected static AbstractCommand instance;

    private final String name;
    private final String permission;
    private final boolean playersOnly;

    protected AbstractCommand(String name, String permission, boolean playersOnly) {
        this.name = name;
        this.permission = permission;
        this.playersOnly = playersOnly;

        instance = this;
    }

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

    public abstract void onExecute(CommandSender sender, List<String> args);
}
