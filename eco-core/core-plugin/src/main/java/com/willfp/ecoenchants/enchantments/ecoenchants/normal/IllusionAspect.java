package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class IllusionAspect extends EcoEnchant {
    public IllusionAspect() {
        super(
                "illusion_aspect", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (!EnchantmentUtils.isFullyChargeIfRequired(this, attacker)) {
            return;
        }


        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, level * 10 + 15, level));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, level * 10 + 15, level));
    }
}
