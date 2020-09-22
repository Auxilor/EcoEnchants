package com.willfp.ecoenchants.command.commands;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.EnchantmentTarget;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

import java.util.List;

public final class CommandEcoreload extends AbstractCommand {
    public CommandEcoreload() {
        super("ecoreload", "ecoenchants.reload", false);
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        reload();
        sender.sendMessage(ConfigManager.getLang().getMessage("reloaded"));
    }

    public static void reload() {
        ConfigManager.updateConfigs();
        EnchantmentRarity.update();
        EnchantmentTarget.update();
        EcoEnchants.update();
        EnchantDisplay.update();

        EcoEnchants.getAll().forEach((ecoEnchant -> {
            HandlerList.unregisterAll(ecoEnchant);

            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
                if(ecoEnchant.isEnabled()) {
                    Bukkit.getPluginManager().registerEvents(ecoEnchant, EcoEnchantsPlugin.getInstance());
                }
            }, 1);
        }));
    }
}
