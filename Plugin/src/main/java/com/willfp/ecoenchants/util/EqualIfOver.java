package com.willfp.ecoenchants.util;

public class EqualIfOver {

    /**
     * If value is above maximum, set it to maximum
     *
     * @param toChange The value to test
     * @param limit    The maximum
     * @return The new value
     */
    public static int equalIfOver(int toChange, int limit) {
        if(toChange > limit) {
            toChange = limit;
        }
        return toChange;
    }

    /**
     * If value is above maximum, set it to maximum
     *
     * @param toChange The value to test
     * @param limit    The maximum
     * @return The new value
     */
    public static double equalIfOver(double toChange, double limit) {
        if(toChange > limit) {
            toChange = limit;
        }
        return toChange;
    }
}
