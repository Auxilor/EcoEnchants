package com.willfp.ecoenchants.command.commands;

import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.loader.Loader;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandEcoreload extends AbstractCommand {
    public CommandEcoreload() {
        super("ecoreload", "ecoenchants.reload", false);
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        Loader.reload();
        sender.sendMessage(ConfigManager.getLang().getMessage("reloaded"));
    }
}
