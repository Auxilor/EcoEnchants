package com.willfp.ecoenchants.command.commands;

import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public final class CommandEcoskip extends AbstractCommand {
    private CommandEcoskip() {
        super("ecoskip", "ecoenchants.skip", true);
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return;
        }
        if(meta.getPersistentDataContainer().has(EnchantDisplay.KEY_SKIP, PersistentDataType.INTEGER)) {
            meta.getPersistentDataContainer().remove(EnchantDisplay.KEY_SKIP);
            player.sendMessage(ConfigManager.getLang().getMessage("skip-removed"));
        } else {
            meta.getPersistentDataContainer().set(EnchantDisplay.KEY_SKIP, PersistentDataType.INTEGER, 1);
            player.sendMessage(ConfigManager.getLang().getMessage("skip-added"));
        }
        item.setItemMeta(meta);
    }
}
