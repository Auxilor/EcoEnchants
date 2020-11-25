package com.willfp.ecoenchants.biomes;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.biomes.enchants.defensive.*;
import com.willfp.ecoenchants.biomes.enchants.offensive.*;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.extensions.Extension;
import org.bukkit.Bukkit;

public class BiomesMain extends Extension {
    public static final EcoEnchant ELEVATION = new Elevation();
    public static final EcoEnchant GLACIAL = new Glacial();
    public static final EcoEnchant HEAT_TREATED = new HeatTreated();
    public static final EcoEnchant OCEANIC = new Oceanic();
    public static final EcoEnchant TROPICAL = new Tropical();
    public static final EcoEnchant ALTITUDE = new Altitude();
    public static final EcoEnchant AQUAMARINE = new Aquamarine();
    public static final EcoEnchant DEHYDRATION = new Dehydration();
    public static final EcoEnchant ICELORD = new Icelord();
    public static final EcoEnchant RAINFOREST = new Rainforest();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(ELEVATION, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(GLACIAL, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(HEAT_TREATED, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(OCEANIC, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(TROPICAL, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(ALTITUDE, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(AQUAMARINE, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(DEHYDRATION, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(ICELORD, EcoEnchantsPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(RAINFOREST, EcoEnchantsPlugin.getInstance());
    }

    @Override
    public void onDisable() {

    }
}
