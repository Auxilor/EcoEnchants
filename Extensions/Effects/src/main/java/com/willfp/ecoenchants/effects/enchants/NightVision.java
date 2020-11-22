package com.willfp.ecoenchants.effects.enchants;

import com.willfp.ecoenchants.effects.EffectsEnchantment;
import org.bukkit.potion.PotionEffectType;

public class NightVision extends EffectsEnchantment {
    public NightVision() {
        super("night_vision", EnchantmentType.NORMAL);
    }

    @Override
    public PotionEffectType getPotionEffect() {
        return PotionEffectType.NIGHT_VISION;
    }
}
