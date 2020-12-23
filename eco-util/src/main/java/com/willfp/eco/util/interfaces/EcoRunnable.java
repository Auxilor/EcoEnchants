package com.willfp.eco.util.interfaces;

public interface EcoRunnable extends Runnable {
    /**
     * The EcoRunnable interface is generally used for repeating tasks.
     * This method is to retrieve the ticks between repetitions.
     *
     * @return The ticks between repetitions.
     */
    long getTime();
}
