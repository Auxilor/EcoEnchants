package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Manyshot extends EcoEnchant {
    public Manyshot() {
        super(
                "manyshot", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onBowShoot(@NotNull final LivingEntity shooter,
                           @NotNull final Arrow arrow,
                           final int level,
                           @NotNull final EntityShootBowEvent event) {
        for (int i = -(level*2); i < (level*2+1); i += 2) {
            if (i == 0) {
                continue;
            }
            Vector velocity = event.getProjectile().getVelocity();

            float radians = (float) ((float) i * Math.toRadians(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "angle")));
            velocity.rotateAroundY(radians);

            Arrow arrow1 = shooter.launchProjectile(Arrow.class, velocity);
            if (EnchantChecks.mainhand(shooter, Enchantment.ARROW_FIRE)) {
                arrow1.setFireTicks(Integer.MAX_VALUE);
            }
            arrow1.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
    }
}
