package com.willfp.ecoenchants.commands;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class CommandEcoskip implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ecoskip")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ConfigManager.getLang().getMessage("not-player"));
                return true;
            }

            if (!sender.hasPermission("ecoenchants.skip")) {
                sender.sendMessage(ConfigManager.getLang().getNoPermission());
                return true;
            }

            Player player = (Player) sender;
            ItemStack item = player.getInventory().getItemInMainHand();
            ItemMeta meta = item.getItemMeta();
            if(meta == null) {
                return true;
            }
            if(meta.getPersistentDataContainer().has(EnchantDisplay.keySkip, PersistentDataType.INTEGER)) {
                meta.getPersistentDataContainer().remove(EnchantDisplay.keySkip);
                player.sendMessage(ConfigManager.getLang().getMessage("skip-removed"));
            } else {
                meta.getPersistentDataContainer().set(EnchantDisplay.keySkip, PersistentDataType.INTEGER, 1);
                player.sendMessage(ConfigManager.getLang().getMessage("skip-added"));
            }
            item.setItemMeta(meta);
        }

        return false;
    }
}
