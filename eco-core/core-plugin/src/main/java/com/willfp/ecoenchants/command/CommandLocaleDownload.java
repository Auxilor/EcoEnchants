package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.config.TransientConfig;
import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.eco.core.web.Paste;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class CommandLocaleDownload extends Subcommand {
    /**
     * Instantiate a new /ecoenchants locale download command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandLocaleDownload(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin, "download", "ecoenchants.command.locale.download", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        if (args.isEmpty()) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-locale"));
            return;
        }

        Paste paste = Paste.getFromHastebin(args.get(0));
        if (paste == null) {
            sender.sendMessage(this.getPlugin().getLangYml().getMessage("invalid-locale"));
            return;
        }

        String contents = paste.getContents();
        Config configuration = new TransientConfig(contents, ConfigType.YAML);

        for (String key : configuration.getKeys(true)) {
            this.getPlugin().getLangYml().set(key, configuration.get(key));
        }

        try {
            this.getPlugin().getLangYml().save();

            for (EcoEnchant value : EcoEnchants.values()) {
                value.getConfig().loadFromLang();
                value.getConfig().save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        sender.sendMessage(this.getPlugin().getLangYml().getMessage("downloaded-locale"));
    }
}
