package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.jetbrains.annotations.NotNull;

public class Inferno extends EcoEnchant {
    public Inferno() {
        super(
                "inferno", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onTridentLaunch(@NotNull final LivingEntity shooter,
                                @NotNull final Trident trident,
                                final int level,
                                final @NotNull ProjectileLaunchEvent event) {
        trident.setFireTicks(Integer.MAX_VALUE);
    }

    @Override
    public void onTridentDamage(@NotNull final LivingEntity attacker,
                                @NotNull final LivingEntity victim,
                                @NotNull final Trident trident,
                                final int level,
                                @NotNull final EntityDamageByEntityEvent event) {
        if (trident.getFireTicks() <= 0) {
            return;
        }

        victim.setFireTicks(100);
    }
}
