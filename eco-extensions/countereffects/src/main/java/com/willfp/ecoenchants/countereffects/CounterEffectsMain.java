package com.willfp.ecoenchants.countereffects;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.ecoenchants.countereffects.enchants.Abundance;
import com.willfp.ecoenchants.countereffects.enchants.Apothecary;
import com.willfp.ecoenchants.countereffects.enchants.Resolve;
import com.willfp.ecoenchants.countereffects.enchants.Vigor;
import com.willfp.ecoenchants.countereffects.enchants.Vivacity;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class CounterEffectsMain extends Extension {
    public static final EcoEnchant APOTHECARY = new Apothecary();
    public static final EcoEnchant ABUNDANCE = new Abundance();
    public static final EcoEnchant VIGOR = new Vigor();
    public static final EcoEnchant VIVACITY = new Vivacity();
    public static final EcoEnchant RESOLVE = new Resolve();

    public CounterEffectsMain(@NotNull final EcoPlugin plugin) {
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
