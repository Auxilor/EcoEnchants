package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.jetbrains.annotations.NotNull;

public class Rapid extends EcoEnchant {
    public Rapid() {
        super(
                "rapid", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onBowShoot(@NotNull LivingEntity shooter, @NotNull Arrow arrow, int level, @NotNull EntityShootBowEvent event) {
        double multiplier = 1 - ((this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-faster-per-level") / 100) * level);

        if (event.getForce() < multiplier)
            return;

        double force = 1 / event.getForce();
        event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(force));
    }
}
