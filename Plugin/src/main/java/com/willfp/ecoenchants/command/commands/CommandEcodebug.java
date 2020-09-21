package com.willfp.ecoenchants.command.commands;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class CommandEcodebug extends AbstractCommand {
    private CommandEcodebug() {
        super("ecodebug", "ecoenchants.ecodebug", true);
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        Bukkit.getLogger().info("--------------- BEGIN DEBUG ----------------");
        Player player = (Player) sender;
        Bukkit.getLogger().info("Running Version: " + EcoEnchantsPlugin.getInstance().getDescription().getVersion());
        Bukkit.getLogger().info("Held Item: " + player.getInventory().getItemInMainHand().toString());
        Bukkit.getLogger().info("EcoEnchants.getAll(): " + EcoEnchants.getAll().toString());
        Bukkit.getLogger().info("Enchantment.values(): " + Arrays.toString(Enchantment.values()));
        try {
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byNameField.setAccessible(true);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            Bukkit.getLogger().info("Enchantment.byName: " + byName.toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Bukkit.getLogger().info("--------------- END DEBUG ----------------");
    }
}
