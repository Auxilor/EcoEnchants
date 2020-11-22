package com.willfp.ecoenchants.effects.enchants;

import com.willfp.ecoenchants.effects.EffectsEnchantment;
import org.bukkit.potion.PotionEffectType;

public class JumpBoost extends EffectsEnchantment {
    public JumpBoost() {
        super("jump_boost", EnchantmentType.NORMAL);
    }

    @Override
    public PotionEffectType getPotionEffect() {
        return PotionEffectType.JUMP;
    }
}
