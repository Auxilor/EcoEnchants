package com.willfp.ecoenchants.countereffects.enchants;

import com.willfp.ecoenchants.countereffects.CounterEffectsEnchantment;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.potion.PotionEffectType;

public class Abundance extends CounterEffectsEnchantment {
    public Abundance() {
        super("abundance", EnchantmentType.NORMAL);
    }

    @Override
    public PotionEffectType[] getPotionEffects() {
        return new PotionEffectType[]{
                PotionEffectType.HUNGER
        };
    }
}
