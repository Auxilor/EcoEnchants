package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class ReversalCurse extends EcoEnchant {
    public ReversalCurse() {
        super(
                "reversal_curse", EnchantmentType.CURSE
        );
    }

    @Override
    public void onBowShoot(@NotNull final LivingEntity shooter, @NotNull final Arrow arrow, final int level, @NotNull final EntityShootBowEvent event) {
        Vector velocity = event.getProjectile().getVelocity();
        velocity.multiply(-1);
        arrow.setVelocity(velocity);
    }
}
