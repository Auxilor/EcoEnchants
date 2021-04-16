package com.willfp.ecoenchants.effects.enchants;

import com.willfp.ecoenchants.effects.EffectsEnchantment;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.potion.PotionEffectType;

public class WaterBreathing extends EffectsEnchantment {
    public WaterBreathing() {
        super("water_breathing", EnchantmentType.NORMAL);
    }

    @Override
    public PotionEffectType getPotionEffect() {
        return PotionEffectType.WATER_BREATHING;
    }
}
