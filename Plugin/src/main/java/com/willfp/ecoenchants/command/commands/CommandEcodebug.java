package com.willfp.ecoenchants.command.commands;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.command.AbstractCommand;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class CommandEcodebug extends AbstractCommand {

    private static final EcoEnchantsPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

    private CommandEcodebug() {
        super("ecodebug", "ecoenchants.ecodebug", true);
    }

    @Override
    public void onExecute(CommandSender sender, List<String> args) {
        PLUGIN.getLogger().info("--------------- BEGIN DEBUG ----------------");
        Player player = (Player) sender;
        PLUGIN.getLogger().info("Running Version: " + EcoEnchantsPlugin.getInstance().getDescription().getVersion());
        PLUGIN.getLogger().info("Held Item: " + player.getInventory().getItemInMainHand().toString());
        PLUGIN.getLogger().info("EcoEnchants.getAll(): " + EcoEnchants.getAll().toString());
        PLUGIN.getLogger().info("Enchantment.values(): " + Arrays.toString(Enchantment.values()));
        try {
            Field byNameField = Enchantment.class.getDeclaredField("byName");
            byNameField.setAccessible(true);
            Map<String, Enchantment> byName = (Map<String, Enchantment>) byNameField.get(null);
            PLUGIN.getLogger().info("Enchantment.byName: " + byName.toString());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        PLUGIN.getLogger().info("--------------- END DEBUG ----------------");
    }
}
