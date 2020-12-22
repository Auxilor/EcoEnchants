package com.willfp.ecoenchants.effects.enchants;

import com.willfp.ecoenchants.effects.EffectsEnchantment;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.potion.PotionEffectType;

public class Regeneration extends EffectsEnchantment {
    public Regeneration() {
        super("regeneration", EnchantmentType.NORMAL);
    }

    @Override
    public PotionEffectType getPotionEffect() {
        return PotionEffectType.REGENERATION;
    }
}
