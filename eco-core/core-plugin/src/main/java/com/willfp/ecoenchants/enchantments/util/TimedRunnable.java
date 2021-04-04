package com.willfp.ecoenchants.enchantments.util;

public interface TimedRunnable extends Runnable {
    /**
     * Get the time between repetitions.
     *
     * @return The time.
     */
    long getTime();
}
