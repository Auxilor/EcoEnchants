package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;

public class PermanenceCurse extends EcoEnchant {
    public PermanenceCurse() {
        super(
                new EcoEnchantBuilder("permanence_curse", EnchantmentType.CURSE, Target.Applicable.ALL, 4.0)
        );
    }

    // START OF LISTENERS

    // Listeners are in anvil listeners
}
