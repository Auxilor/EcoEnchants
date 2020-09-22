package com.willfp.ecoenchants.util;

import com.willfp.ecoenchants.EcoEnchantsPlugin;

public class Logger {
    public static void info(String message) {
        EcoEnchantsPlugin.getInstance().getLogger().info(StringUtils.translate(message));
    }

    public static void warn(String message) {
        EcoEnchantsPlugin.getInstance().getLogger().warning(StringUtils.translate(message));
    }

    public static void error(String message) {
        EcoEnchantsPlugin.getInstance().getLogger().severe(StringUtils.translate(message));
    }
}
