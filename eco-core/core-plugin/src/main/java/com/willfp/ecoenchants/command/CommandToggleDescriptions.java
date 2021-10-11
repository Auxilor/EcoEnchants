package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandToggleDescriptions extends Subcommand {
    /**
     * Instantiate a new command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandToggleDescriptions(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin, "toggledescriptions", "ecoenchants.command.toggledescriptions", true);
    }

    @Override
    public CommandHandler getHandler() {
        return (sender, args) -> {
            if (!((EcoEnchantsPlugin) this.getPlugin()).getDisplayModule().getOptions().getDescriptionOptions().isEnabled()){
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("descriptions-disabled"));
                return;
            }
            Player player = (Player) sender;
            ((EcoEnchantsPlugin) this.getPlugin()).getDataYml().toggleDescriptions(player);
            player.sendMessage(this.getPlugin().getLangYml().getMessage("descriptions-enabled."+((EcoEnchantsPlugin) this.getPlugin()).getDataYml().isDescriptionEnabled(player)));
        };
    }
}
