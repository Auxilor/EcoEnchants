package com.willfp.eco.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class NumberUtils {
    /**
     * Set of roman numerals to look up.
     */
    private static final TreeMap<Integer, String> NUMERALS = new TreeMap<>();

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
     * Bias the input value according to a curve.
     *
     * @param input The input value.
     * @param bias  The bias between -1 and 1, where higher values bias input values to lower output values.
     * @return The biased output.
     */
    public double bias(final double input,
                       final double bias) {
        double k = Math.pow(1 - bias, 3);

        return (input * k) / (input * k - input + 1);
    }

    /**
     * If value is above maximum, set it to maximum.
     *
     * @param toChange The value to test.
     * @param limit    The maximum.
     * @return The new value.
     */
    public int equalIfOver(final int toChange,
                           final int limit) {
        return Math.min(toChange, limit);
    }

    /**
     * If value is above maximum, set it to maximum.
     *
     * @param toChange The value to test.
     * @param limit    The maximum.
     * @return The new value.
     */
    public double equalIfOver(final double toChange,
                              final double limit) {
        return Math.min(toChange, limit);
    }

    /**
     * Get Roman Numeral from number.
     *
     * @param number The number to convert.
     * @return The number, converted to a roman numeral.
     */
    public String toNumeral(final int number) {
        if (number >= 1 && number <= 4096) {
            int l = NUMERALS.floorKey(number);
            if (number == l) {
                return NUMERALS.get(number);
            }
            return NUMERALS.get(l) + toNumeral(number - l);
        } else {
            return String.valueOf(number);
        }
    }

    /**
     * Generate random integer in range.
     *
     * @param min Minimum.
     * @param max Maximum.
     * @return Random integer.
     */
    public int randInt(final int min,
                       final int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * Generate random double in range.
     *
     * @param min Minimum.
     * @param max Maximum.
     * @return Random double.
     */
    public double randFloat(final double min,
                            final double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    /**
     * Generate random double with a triangular distribution.
     *
     * @param minimum Minimum.
     * @param maximum Maximum.
     * @param peak    Peak.
     * @return Random double.
     */
    public double triangularDistribution(final double minimum,
                                         final double maximum,
                                         final double peak) {
        double f = (peak - minimum) / (maximum - minimum);
        double rand = Math.random();
        if (rand < f) {
            return minimum + Math.sqrt(rand * (maximum - minimum) * (peak - minimum));
        } else {
            return maximum - Math.sqrt((1 - rand) * (maximum - minimum) * (maximum - peak));
        }
    }

    /**
     * Get Log base 2 of a number.
     *
     * @param toLog The number.
     * @return The result.
     */
    public int log2(final int toLog) {
        return (int) (Math.log(toLog) / Math.log(2));
    }

    /**
     * Format double to string.
     *
     * @param toFormat The number to format.
     * @return Formatted.
     */
    public String format(final double toFormat) {
        DecimalFormat df = new DecimalFormat("0.00");
        String formatted = df.format(toFormat);

        return formatted.endsWith("00") ? String.valueOf((int) toFormat) : formatted;
    }
}
