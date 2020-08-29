package com.willfp.ecoenchants.util;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/**
 * Class containing methods for striking lightning
 */
public class Lightning {

    /**
     * Strike lightning on player without fire
     *
     * @param victim The entity to smite
     * @param damage The damage to deal
     */
    public static void strike(LivingEntity victim, double damage) {
        if(victim == null) return;

        Location loc = victim.getLocation();

        victim.getWorld().strikeLightningEffect(loc);

        victim.damage(damage);
    }
}
