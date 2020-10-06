package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;

public final class Prosperity extends EcoEnchant {
    public Prosperity() {
        super(
                new EcoEnchantBuilder("prosperity", EnchantmentType.SPECIAL, 5.0)
        );
    }

    // Prosperity listeners are located in Thrive
}
