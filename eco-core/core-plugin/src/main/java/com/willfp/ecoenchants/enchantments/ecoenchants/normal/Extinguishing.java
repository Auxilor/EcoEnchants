package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public class Extinguishing extends EcoEnchant {
    public Extinguishing() {
        super(
                "extinguishing", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onDamageWearingArmor(@NotNull final LivingEntity victim,
                                     final int level,
                                     @NotNull final EntityDamageEvent event) {
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        victim.setFireTicks(0);
    }
}
