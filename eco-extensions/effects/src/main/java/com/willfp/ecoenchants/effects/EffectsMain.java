package com.willfp.ecoenchants.effects;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.ecoenchants.effects.enchants.JumpBoost;
import com.willfp.ecoenchants.effects.enchants.NightVision;
import com.willfp.ecoenchants.effects.enchants.Regeneration;
import com.willfp.ecoenchants.effects.enchants.Speed;
import com.willfp.ecoenchants.effects.enchants.WaterBreathing;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class EffectsMain extends Extension {
    public static final EcoEnchant JUMP_BOOST = new JumpBoost();
    public static final EcoEnchant NIGHT_VISION = new NightVision();
    public static final EcoEnchant REGENERATION = new Regeneration();
    public static final EcoEnchant SPEED = new Speed();
    public static final EcoEnchant WATER_BREATHING = new WaterBreathing();

    public EffectsMain(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        // Handled by super
    }

    @Override
    public void onDisable() {
        // Handled by super
    }
}
