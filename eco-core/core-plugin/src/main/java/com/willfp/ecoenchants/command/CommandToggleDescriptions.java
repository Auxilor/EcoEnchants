package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.data.storage.PlayerProfile;
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
            if (!((EcoEnchantsPlugin) this.getPlugin()).getDisplayModule().getOptions().getDescriptionOptions().isEnabled()) {
                sender.sendMessage(this.getPlugin().getLangYml().getMessage("descriptions-disabled"));
                return;
            }
            Player player = (Player) sender;
            PlayerProfile profile = PlayerProfile.getProfile(player);
            boolean currentStatus = profile.read("descriptions", true);
            currentStatus = !currentStatus;
            profile.write("descriptions", currentStatus);
            if (currentStatus) {
                player.sendMessage(this.getPlugin().getLangYml().getMessage("enabled-descriptions"));
            } else {
                player.sendMessage(this.getPlugin().getLangYml().getMessage("disabled-descriptions"));
            }
        };
    }
}
