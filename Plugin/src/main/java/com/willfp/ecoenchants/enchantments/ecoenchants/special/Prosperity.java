package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.nms.Target;

public class Prosperity extends EcoEnchant {
    public Prosperity() {
        super(
                new EcoEnchantBuilder("prosperity", EnchantmentType.SPECIAL, Target.Applicable.ARMOR, 4.0)
        );
    }

    // Prosperity listeners are located in Thrive
}
