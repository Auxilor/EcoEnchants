package com.willfp.ecoenchants.util;

import java.util.TreeMap;

public class NumberUtils {

    private final static TreeMap<Integer, String> NUMERALS = new TreeMap<>();

    static {

        NUMERALS.put(1000, "M");
        NUMERALS.put(900, "CM");
        NUMERALS.put(500, "D");
        NUMERALS.put(400, "CD");
        NUMERALS.put(100, "C");
        NUMERALS.put(90, "XC");
        NUMERALS.put(50, "L");
        NUMERALS.put(40, "XL");
        NUMERALS.put(10, "X");
        NUMERALS.put(9, "IX");
        NUMERALS.put(5, "V");
        NUMERALS.put(4, "IV");
        NUMERALS.put(1, "I");

    }

    /**
     * Bias the input value according to a curve
     * @param input The input value
     * @param bias The bias between -1 and 1, where higher values bias input values to lower output values
     * @return The biased output
     */
    public static double bias(double input, double bias) {
        double k = Math.pow(1-bias, 3);

        return (input * k) / (input * k - input + 1);
    }

    /**
     * If value is above maximum, set it to maximum
     * @param toChange The value to test
     * @param limit The maximum
     * @return The new value
     */
    public static int equalIfOver(int toChange, int limit) {
        if (toChange > limit) {
            toChange = limit;
        }
        return toChange;
    }

    /**
     * If value is above maximum, set it to maximum
     * @param toChange The value to test
     * @param limit The maximum
     * @return The new value
     */
    public static double equalIfOver(double toChange, double limit) {
        if (toChange > limit) {
            toChange = limit;
        }
        return toChange;
    }

    /**
     * Get Roman Numeral from number
     * @param number The number to convert
     * @return The number, converted to a roman numeral
     */
    public static String toNumeral(int number) {
        if (number >= 1 && number <= 4096) {
            int l = NUMERALS.floorKey(number);
            if (number == l) {
                return NUMERALS.get(number);
            }
            return NUMERALS.get(l) + toNumeral(number - l);
        } else return String.valueOf(number);
    }

    /**
     * Generate random integer in range
     * @param min Minimum
     * @param max Maximum
     * @return Random integer
     */
    public static int randInt(int min, int max) {
        return (int) ((long) min + Math.random() * ((long) max - min + 1));
    }

    /**
     * Generate random double in range
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
     * @param a Minimum
     * @param b Maximum
     * @param c Peak
     * @return Random double
     */
    public static double triangularDistribution(double a, double b, double c) {
        double F = (c - a) / (b - a);
        double rand = Math.random();
        if (rand < F) {
            return a + Math.sqrt(rand * (b - a) * (c - a));
        } else {
            return b - Math.sqrt((1 - rand) * (b - a) * (b - c));
        }
    }
}
