package com.willfp.ecoenchants.command;

import com.willfp.eco.core.command.impl.Subcommand;
import com.willfp.eco.core.web.Paste;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.StringReader;
import java.util.List;

public class CommandLocaleExport extends Subcommand {
    /**
     * Instantiate a new /ecoenchants locale export command handler.
     *
     * @param plugin The plugin for the commands to listen for.
     */
    public CommandLocaleExport(@NotNull final EcoEnchantsPlugin plugin) {
        super(plugin, "export", "ecoenchants.command.locale.export", false);
    }

    @Override
    public void onExecute(@NotNull final CommandSender sender,
                          @NotNull final List<String> args) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new StringReader(this.getPlugin().getLangYml().toPlaintext()));
        for (EcoEnchant enchant : EcoEnchants.values()) {
            configuration.set("enchantments." + enchant.getKey().getKey() + ".name", enchant.getConfig().getHandle().getString("name"));
            configuration.set("enchantments." + enchant.getKey().getKey() + ".description", enchant.getConfig().getHandle().getString("description"));
        }

        Paste paste = new Paste(configuration.saveToString());

        paste.getHastebinToken(token -> {
            if (token.length() > 15) {
                sender.sendMessage(
                        this.getPlugin().getLangYml().getMessage("export-failed")
                );
            } else {
                sender.sendMessage(
                        this.getPlugin().getLangYml().getMessage("link-to-locale").replace(
                                "%token%", token
                        )
                );
            }
        });
    }
}
