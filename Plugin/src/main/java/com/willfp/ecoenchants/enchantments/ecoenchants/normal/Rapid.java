package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityShootBowEvent;
public final class Rapid extends EcoEnchant {
    public Rapid() {
        super(
                new EcoEnchantBuilder("rapid", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onBowShoot(LivingEntity shooter, Arrow arrow, int level, EntityShootBowEvent event) {
        double multiplier = 1 - ((this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-faster-per-level")/100) * level);

        if(event.getForce() < multiplier)
            return;

        double force = 1/event.getForce();
        event.getProjectile().setVelocity(event.getProjectile().getVelocity().multiply(force));
    }
}
