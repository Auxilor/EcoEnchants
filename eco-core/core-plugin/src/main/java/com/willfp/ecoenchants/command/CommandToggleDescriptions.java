package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.CommandHandler;
import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.eco.core.data.keys.PersistentDataKey;
import com.willfp.eco.core.data.keys.PersistentDataKeyType;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandToggleDescriptions extends Subcommand {
    /**
     * Persistent data key for descriptions.
     */
    private final PersistentDataKey<Boolean> descriptionsKey = new PersistentDataKey<>(
            this.getPlugin().getNamespacedKeyFactory().create("descriptions_enabled"),
            PersistentDataKeyType.BOOLEAN,
            true
    );

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
            PlayerProfile profile = PlayerProfile.load(player);
            boolean currentStatus = profile.read(descriptionsKey);
            currentStatus = !currentStatus;
            profile.write(descriptionsKey, currentStatus);
            if (currentStatus) {
                player.sendMessage(this.getPlugin().getLangYml().getMessage("enabled-descriptions"));
            } else {
                player.sendMessage(this.getPlugin().getLangYml().getMessage("disabled-descriptions"));
            }
        };
    }
}
