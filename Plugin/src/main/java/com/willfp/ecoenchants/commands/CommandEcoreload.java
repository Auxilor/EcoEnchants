package com.willfp.ecoenchants.commands;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.EnchantmentRarity;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class CommandEcoreload implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ecoreload")) {
            if (sender instanceof Player) {
                if (!sender.hasPermission("ecoenchants.reload")) {
                    sender.sendMessage(ConfigManager.getLang().getNoPermission());
                    return true;
                }
            }
            ConfigManager.updateConfigs();
            EnchantmentRarity.update();
            EcoEnchants.update();
            EnchantDisplay.update();

            EcoEnchants.getAll().forEach((ecoEnchant -> {
                HandlerList.unregisterAll(ecoEnchant);

                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    if(!ecoEnchant.isDisabled()) {
                        Bukkit.getPluginManager().registerEvents(ecoEnchant, Main.getInstance());
                    }
                }, 1);
            }));
            sender.sendMessage(ConfigManager.getLang().getMessage("reloaded"));
        }

        return false;
    }
}
