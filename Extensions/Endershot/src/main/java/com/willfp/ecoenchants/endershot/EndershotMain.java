package com.willfp.ecoenchants.endershot;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.extensions.Extension;
import org.bukkit.Bukkit;

public class EndershotMain extends Extension {
    public static final EcoEnchant ENDERSHOT = new Endershot();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(ENDERSHOT, EcoEnchantsPlugin.getInstance());
    }

    @Override
    public void onDisable() {

    }
}
