package com.willfp.eco.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class LightningUtils {
    /**
     * Strike lightning on player without fire.
     *
     * @param victim The entity to smite.
     * @param damage The damage to deal.
     */
    public void strike(@NotNull final LivingEntity victim,
                       final double damage) {
        Location loc = victim.getLocation();

        victim.getWorld().strikeLightningEffect(loc);

        victim.damage(damage);
    }
}
