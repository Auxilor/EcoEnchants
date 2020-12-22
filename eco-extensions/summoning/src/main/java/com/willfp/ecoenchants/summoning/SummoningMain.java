package com.willfp.ecoenchants.summoning;

import com.willfp.eco.util.extensions.Extension;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.summoning.enchants.Crawler;
import com.willfp.ecoenchants.summoning.enchants.Firestorm;
import com.willfp.ecoenchants.summoning.enchants.Ghoul;
import com.willfp.ecoenchants.summoning.enchants.Metallic;
import com.willfp.ecoenchants.summoning.enchants.Mortality;

public class SummoningMain extends Extension {
    public static final EcoEnchant METALLIC = new Metallic();
    public static final EcoEnchant FIRESTORM = new Firestorm();
    public static final EcoEnchant MORTALITY = new Mortality();
    public static final EcoEnchant GHOUL = new Ghoul();
    public static final EcoEnchant CRAWLER = new Crawler();

    @Override
    public void onEnable() {
        // Handled by super
    }

    @Override
    public void onDisable() {
        // Handled by super
    }
}
