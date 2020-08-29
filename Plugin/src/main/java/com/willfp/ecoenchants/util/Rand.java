package com.willfp.ecoenchants.util;

/**
 * Simple class containing random methods
 */
public class Rand {
    /**
     * Generate random integer in range
     *
     * @param min Minimum
     * @param max Maximum
     * @return Random integer
     */
    public static int randInt(int min, int max) {
        return (int) ((long) min + Math.random() * ((long) max - min + 1));
    }

    /**
     * Generate random double in range
     *
     * @param min Minimum
     * @param max Maximum
     * @return Random double
     */
    public static double randFloat(double min, double max) {
        java.util.Random rand = new java.util.Random();
        return rand.nextFloat() * (max - min) + min;
    }

    /**
     * Generate random double with a triangular distribution
     *
     * @param a Minimum
     * @param b Maximum
     * @param c Peak
     * @return Random double
     */
    public static double triangularDistribution(double a, double b, double c) {
        double F = (c - a) / (b - a);
        double rand = Math.random();
        if(rand < F) {
            return a + Math.sqrt(rand * (b - a) * (c - a));
        } else {
            return b - Math.sqrt((1 - rand) * (b - a) * (b - c));
        }
    }
}
