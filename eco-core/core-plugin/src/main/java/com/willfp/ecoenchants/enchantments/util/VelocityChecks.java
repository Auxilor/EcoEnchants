package com.willfp.ecoenchants.enchantments.util;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class VelocityChecks {
    /**
     * Checks to see if the velocity is unsafe. This is taken from Papers 0054-Add-velocity-warnings.patch.
     *
     * @param vel The velocity.
     * @return If unsafe.
     */
    public static boolean isUnsafeVelocity(@NotNull final Vector vel) {
        final double x = vel.getX();
        final double y = vel.getY();
        final double z = vel.getZ();

        return x > 4 || x < -4 || y > 4 || y < -4 || z > 4 || z < -4;
    }
}
