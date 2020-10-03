package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.util.VectorUtils;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Infuriate extends EcoEnchant {
    public Infuriate() {
        super(
                new EcoEnchantBuilder("infuriate", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDeflect(Player blocker, LivingEntity attacker, int level, EntityDamageByEntityEvent event) {
        if(!EnchantmentUtils.passedChance(this, level))
            return;

        double distancePerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level");
        final double distance = distancePerLevel * level;

        for (Entity e : attacker.getWorld().getNearbyEntities(attacker.getLocation(), distance, distance, distance)) {
            if(!(e instanceof Monster)) continue;

            if(e instanceof PigZombie) {
                ((PigZombie) e).setAngry(true);
            }

            ((Monster) e).setTarget(attacker);

            Vector vector = attacker.getLocation().toVector().clone().subtract(e.getLocation().toVector()).normalize().multiply(0.23d);

            if(VectorUtils.isFinite(vector)) {
                e.setVelocity(vector);
            }
        }
    }
}
