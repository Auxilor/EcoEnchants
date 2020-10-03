package com.willfp.ecoenchants.util;

import com.willfp.ecoenchants.EcoEnchantsPlugin;

public class Logger {
    private static final EcoEnchantsPlugin INSTANCE = EcoEnchantsPlugin.getInstance();

    public static void info(String message) {
        INSTANCE.getLogger().info(StringUtils.translate(message));
    }

    public static void warn(String message) {
        INSTANCE.getLogger().warning(StringUtils.translate(message));
    }

    public static void error(String message) {
        INSTANCE.getLogger().severe(StringUtils.translate(message));
    }
}
