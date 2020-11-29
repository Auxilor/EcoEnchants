package com.willfp.ecoenchants.util.internal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.util.StringUtils;

/**
 * The internal logger for EcoEnchants
 * Automatically formats all inputs using {@link StringUtils#translate(String)}
 */
public class Logger {
    private static final EcoEnchantsPlugin INSTANCE = EcoEnchantsPlugin.getInstance();

    /**
     * Print an info (neutral) message to console
     *
     * @param message The message to send
     */
    public static void info(String message) {
        INSTANCE.getLogger().info(StringUtils.translate(message));
    }

    /**
     * Print a warning to console
     *
     * @param message The warning
     */
    public static void warn(String message) {
        INSTANCE.getLogger().warning(StringUtils.translate(message));
    }

    /**
     * Print an error to console
     *
     * @param message The error
     */
    public static void error(String message) {
        INSTANCE.getLogger().severe(StringUtils.translate(message));
    }
}
