package com.willfp.ecoenchants.util;

import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class LocationUtils {
    public static boolean isFinite(Vector vector) {
        try {
            NumberConversions.checkFinite(vector.getX(), "x not finite");
            NumberConversions.checkFinite(vector.getY(), "y not finite");
            NumberConversions.checkFinite(vector.getZ(), "z not finite");
        } catch(IllegalArgumentException e) {
            return false;
        }

        return true;
    }
}
