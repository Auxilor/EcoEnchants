package com.willfp.ecoenchants.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class NumberUtils {
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

        LinkedHashMap<String, Integer> roman_numerals = new LinkedHashMap<String, Integer>();
        roman_numerals.put("M", 1000);
        roman_numerals.put("CM", 900);
        roman_numerals.put("D", 500);
        roman_numerals.put("CD", 400);
        roman_numerals.put("C", 100);
        roman_numerals.put("XC", 90);
        roman_numerals.put("L", 50);
        roman_numerals.put("XL", 40);
        roman_numerals.put("X", 10);
        roman_numerals.put("IX", 9);
        roman_numerals.put("V", 5);
        roman_numerals.put("IV", 4);
        roman_numerals.put("I", 1);
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, Integer> entry : roman_numerals.entrySet()) {
            int matches = number / entry.getValue();
            res.append(repeat(entry.getKey(), matches));
            number = number % entry.getValue();
        }
        return res.toString();
    }

    private static String repeat(String s, int n) {
        if (s == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
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
