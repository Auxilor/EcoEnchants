package com.willfp.ecoenchants.effects;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.effects.enchants.*;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.extensions.Extension;
import org.bukkit.Bukkit;

public class EffectsMain extends Extension {
    public static final EcoEnchant JUMP_BOOST = new JumpBoost();
    public static final EcoEnchant NIGHT_VISION = new NightVision();
    public static final EcoEnchant REGENERATION = new Regeneration();
    public static final EcoEnchant SPEED = new Speed();
    public static final EcoEnchant WATER_BREATHING = new WaterBreathing();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(JUMP_BOOST, this.plugin);
        Bukkit.getPluginManager().registerEvents(NIGHT_VISION, this.plugin);
        Bukkit.getPluginManager().registerEvents(REGENERATION, this.plugin);
        Bukkit.getPluginManager().registerEvents(SPEED, this.plugin);
        Bukkit.getPluginManager().registerEvents(WATER_BREATHING, this.plugin);
    }

    @Override
    public void onDisable() {

    }
}
