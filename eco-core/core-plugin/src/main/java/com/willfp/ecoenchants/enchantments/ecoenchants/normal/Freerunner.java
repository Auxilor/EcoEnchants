package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class Freerunner extends EcoEnchant {
    public Freerunner() {
        super(
                "freerunner", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onFallDamage(@NotNull final LivingEntity faller,
                             final int level,
                             @NotNull final EntityDamageEvent event) {

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        event.setCancelled(true);
    }
}
