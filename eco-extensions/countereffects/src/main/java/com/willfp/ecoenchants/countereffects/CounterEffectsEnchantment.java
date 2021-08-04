package com.willfp.ecoenchants.countereffects;

import com.willfp.eco.core.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class CounterEffectsEnchantment extends EcoEnchant {
    protected CounterEffectsEnchantment(@NotNull final String key,
                                        @NotNull final EnchantmentType type,
                                        @NotNull final Prerequisite... prerequisites) {
        super(key, type, prerequisites);
    }

    public abstract PotionEffectType[] getPotionEffects();

    @EventHandler
    public void onEffect(@NotNull final EntityPotionEffectEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof LivingEntity livingEntity)) {
            return;
        }

        if (event.getNewEffect() == null) {
            return;
        }

        int level = EnchantChecks.getArmorPoints(livingEntity, this);

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (Arrays.asList(this.getPotionEffects()).contains(event.getNewEffect().getType())) {
            event.setCancelled(true);
        }
    }
}
