package com.willfp.ecoenchants.command.commands;

import com.willfp.eco.util.command.AbstractCommand;
import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandEcoreload extends AbstractCommand {
    public CommandEcoreload(AbstractEcoPlugin plugin) {
        super(plugin, "ecoreload", "ecoenchants.reload", false);
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        this.plugin.reload();
        sender.sendMessage(Configs.LANG.getMessage("reloaded"));
    }
}
