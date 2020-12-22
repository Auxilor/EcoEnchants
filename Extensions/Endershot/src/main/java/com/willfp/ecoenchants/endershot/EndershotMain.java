package com.willfp.ecoenchants.endershot;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.extensions.Extension;
import org.bukkit.Bukkit;

public class EndershotMain extends Extension {
    public static final EcoEnchant ENDERSHOT = new Endershot();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(ENDERSHOT, this.plugin);
    }

    @Override
    public void onDisable() {

    }
}
