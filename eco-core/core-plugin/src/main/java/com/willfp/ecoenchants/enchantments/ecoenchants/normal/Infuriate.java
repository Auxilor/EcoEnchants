package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Infuriate extends EcoEnchant {
    public Infuriate() {
        super(
                "infuriate", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onDeflect(@NotNull final Player blocker,
                          @NotNull final LivingEntity attacker,
                          final int level,
                          @NotNull final EntityDamageByEntityEvent event) {
        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        double distancePerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level");
        final double distance = distancePerLevel * level;

        for (Entity e : attacker.getWorld().getNearbyEntities(attacker.getLocation(), distance, distance, distance)) {
            if (!(e instanceof Monster)) {
                continue;
            }

            if (e instanceof PigZombie) {
                ((PigZombie) e).setAngry(true);
            }

            ((Monster) e).setTarget(attacker);

            Vector vector = attacker.getLocation().toVector().clone().subtract(e.getLocation().toVector()).normalize().multiply(0.23d);

            if (VectorUtils.isFinite(vector)) {
                e.setVelocity(vector);
            }
        }
    }
}
