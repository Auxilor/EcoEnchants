package com.willfp.ecoenchants.countereffects.enchants;

import com.willfp.ecoenchants.countereffects.CounterEffectsEnchantment;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.potion.PotionEffectType;

public class Vigor extends CounterEffectsEnchantment {
    public Vigor() {
        super("vigor", EnchantmentType.NORMAL);
    }

    @Override
    public PotionEffectType[] getPotionEffects() {
        return new PotionEffectType[]{
                PotionEffectType.CONFUSION
        };
    }
}
