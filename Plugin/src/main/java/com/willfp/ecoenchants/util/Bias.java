package com.willfp.ecoenchants.util;

public class Bias {

    /**
     * Bias the input value according to a curve
     *
     * @param input The input value
     * @param bias  The bias between -1 and 1, where higher values bias input values to lower output values
     * @return The biased output
     */
    public static double bias(double input, double bias) {
        double k = Math.pow(1 - bias, 3);

        return (input * k) / (input * k - input + 1);
    }
}
