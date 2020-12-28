package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Rage extends EcoEnchant {
    public Rage() {
        super(
                "rage", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Arrow arrow, int level, @NotNull EntityDamageByEntityEvent event) {
        if (!EnchantmentUtils.passedChance(this, level))
            return;

        double distancePerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level");
        final double distance = distancePerLevel * level;

        for (Entity e : victim.getWorld().getNearbyEntities(victim.getLocation(), distance, distance, distance)) {
            if (!(e instanceof Monster)) continue;

            if (e instanceof PigZombie) {
                ((PigZombie) e).setAngry(true);
            }

            ((Monster) e).setTarget(victim);

            Vector vector = attacker.getLocation().toVector().clone().subtract(e.getLocation().toVector()).normalize().multiply(0.23d);

            if (VectorUtils.isFinite(vector)) {
                e.setVelocity(vector);
            }
        }
    }
}
