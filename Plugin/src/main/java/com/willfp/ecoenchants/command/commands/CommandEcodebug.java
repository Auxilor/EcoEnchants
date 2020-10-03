package com.willfp.ecoenchants.command.commands;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.Logger;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class CommandEcodebug extends AbstractCommand {
    public CommandEcodebug() {
        super("ecodebug", "ecoenchants.ecodebug", true);
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        Logger.info("--------------- BEGIN DEBUG ----------------");
        Player player = (Player) sender;
        Logger.info("Running Version: " + EcoEnchantsPlugin.getInstance().getDescription().getVersion());
        Logger.info("Held Item: " + player.getInventory().getItemInMainHand().toString());
        Logger.info("EcoEnchants.getAll(): " + EcoEnchants.getAll().toString());
        Logger.info("Enchantment.values(): " + Arrays.toString(Enchantment.values()));
        try {
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byNameField.setAccessible(true);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            Logger.info("Enchantment.byName: " + byName.toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Logger.info("--------------- END DEBUG ----------------");
    }
}
