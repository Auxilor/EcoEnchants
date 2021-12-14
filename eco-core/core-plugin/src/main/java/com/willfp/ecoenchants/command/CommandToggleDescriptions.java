package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.eco.core.data.keys.PersistentDataKey;
import com.willfp.eco.core.data.keys.PersistentDataKeyType;
import com.willfp.eco.util.NamespacedKeyUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandToggleDescriptions extends Subcommand {
    /**
     * Persistent data key for descriptions.
     */
    public static final PersistentDataKey<Boolean> DESCRIPTIONS_KEY = new PersistentDataKey<>(
            NamespacedKeyUtils.create("ecoenchants", "descriptions_enabled"),
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
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        if (!((EcoEnchantsPlugin) this.getPlugin()).getDisplayModule().getOptions().getDescriptionOptions().isEnabled()) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("descriptions-disabled"));
            return;
        }
        Player player = (Player) sender;
        PlayerProfile profile = PlayerProfile.load(player);
        boolean currentStatus = profile.read(DESCRIPTIONS_KEY);
        currentStatus = !currentStatus;
        profile.write(DESCRIPTIONS_KEY, currentStatus);
        if (currentStatus) {
            player.sendMessage(this.getPlugin().getLangYml().getMessage("enabled-descriptions"));
        } else {
            player.sendMessage(this.getPlugin().getLangYml().getMessage("disabled-descriptions"));
        }
    }
}
