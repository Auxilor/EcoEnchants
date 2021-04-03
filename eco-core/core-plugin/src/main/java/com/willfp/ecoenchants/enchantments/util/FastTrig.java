package com.willfp.ecoenchants.enchantments.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FastTrig {
    /**
     * Precision.
     */
    private static final int PRECISION = 100;

    /**
     * Modulus.
     */
    private static final int MODULUS = 360 * PRECISION;

    /**
     * Sin lookup table.
     */
    private static final double[] SIN_LOOKUP = new double[MODULUS];

    private static double sinLookup(final int a) {
        return a >= 0 ? SIN_LOOKUP[a % MODULUS] : -SIN_LOOKUP[-a % MODULUS];
    }

    /**
     * Get the sin of a number.
     *
     * @param a The number.
     * @return The sin.
     */
    public static double sin(final double a) {
        return sinLookup((int) (a * PRECISION + 0.5f));
    }

    /**
     * Get the cosine of a number.
     *
     * @param a The number.
     * @return The cosine.
     */
    public static double cos(final double a) {
        return sinLookup((int) ((a + 90f) * PRECISION + 0.5f));
    }


    static {
        for (int i = 0; i < SIN_LOOKUP.length; i++) {
            SIN_LOOKUP[i] = Math.sin((i * Math.PI) / (PRECISION * 180));
        }
    }
}
