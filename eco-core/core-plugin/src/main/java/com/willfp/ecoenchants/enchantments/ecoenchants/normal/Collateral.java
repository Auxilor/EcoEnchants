package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.jetbrains.annotations.NotNull;

public class Collateral extends EcoEnchant {
    public Collateral() {
        super(
                "collateral", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBowShoot(@NotNull final LivingEntity shooter,
                           @NotNull final Arrow arrow,
                           final int level,
                           @NotNull final EntityShootBowEvent event) {
        arrow.setPierceLevel(level);
    }
}
