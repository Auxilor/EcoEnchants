package com.willfp.ecoenchants.summoning;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.extensions.Extension;
import com.willfp.ecoenchants.summoning.enchants.*;
import org.bukkit.Bukkit;

public class SummoningMain extends Extension {
    public static final EcoEnchant METALLIC = new Metallic();
    public static final EcoEnchant FIRESTORM = new Firestorm();
    public static final EcoEnchant MORTALITY = new Mortality();
    public static final EcoEnchant GHOUL = new Ghoul();
    public static final EcoEnchant CRAWLER = new Crawler();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(FIRESTORM, this.plugin);
        Bukkit.getPluginManager().registerEvents(METALLIC, this.plugin);
        Bukkit.getPluginManager().registerEvents(MORTALITY, this.plugin);
        Bukkit.getPluginManager().registerEvents(GHOUL, this.plugin);
        Bukkit.getPluginManager().registerEvents(CRAWLER, this.plugin);
    }

    @Override
    public void onDisable() {

    }
}
