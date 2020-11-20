package com.willfp.ecoenchants.firewand;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.extensions.Extension;
import org.bukkit.Bukkit;

public class FirewandMain extends Extension {
    public static final EcoEnchant FIREWAND = new Firewand();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(FIREWAND, EcoEnchantsPlugin.getInstance());
    }

    @Override
    public void onDisable() {

    }
}
