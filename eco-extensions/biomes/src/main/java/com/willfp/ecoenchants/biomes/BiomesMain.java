package com.willfp.ecoenchants.biomes;


import com.willfp.eco.util.extensions.Extension;
import com.willfp.ecoenchants.biomes.enchants.defensive.Elevation;
import com.willfp.ecoenchants.biomes.enchants.defensive.Glacial;
import com.willfp.ecoenchants.biomes.enchants.defensive.HeatTreated;
import com.willfp.ecoenchants.biomes.enchants.defensive.Oceanic;
import com.willfp.ecoenchants.biomes.enchants.defensive.Tropical;
import com.willfp.ecoenchants.biomes.enchants.offensive.Altitude;
import com.willfp.ecoenchants.biomes.enchants.offensive.Aquamarine;
import com.willfp.ecoenchants.biomes.enchants.offensive.Dehydration;
import com.willfp.ecoenchants.biomes.enchants.offensive.Icelord;
import com.willfp.ecoenchants.biomes.enchants.offensive.Rainforest;
import com.willfp.ecoenchants.enchantments.EcoEnchant;

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

    }

    @Override
    public void onDisable() {

    }
}
