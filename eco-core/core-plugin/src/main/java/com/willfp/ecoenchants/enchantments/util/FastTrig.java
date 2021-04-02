package com.willfp.ecoenchants.enchantments.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FastTrig {
    /**
     * Precision.
     */
    private static final int precision = 100;

    /**
     * Modulus.
     */
    private static final int modulus = 360 * precision;

    /**
     * Sin lookup table.
     */
    private static final double[] sin = new double[modulus];

    private static double sinLookup(final int a) {
        return a >= 0 ? sin[a % modulus] : -sin[-a % modulus];
    }

    /**
     * Get the sin of a number.
     *
     * @param a The number.
     * @return The sin.
     */
    public static double sin(final double a) {
        return sinLookup((int) (a * precision + 0.5f));
    }

    /**
     * Get the cosine of a number.
     *
     * @param a The number.
     * @return The cosine.
     */
    public static double cos(final double a) {
        return sinLookup((int) ((a + 90f) * precision + 0.5f));
    }


    static {
        for (int i = 0; i < sin.length; i++) {
            sin[i] = Math.sin((i * Math.PI) / (precision * 180));
        }
    }
}
