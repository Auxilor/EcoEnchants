package com.willfp.ecoenchants.util;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;

public class Logger {
    public static void info(String message) {
        Bukkit.getLogger().info(StringUtils.translate(message));
    }

    public static void warn(String message) {
        Bukkit.getLogger().warning(StringUtils.translate(message));
    }

    public static void error(String message) {
        Bukkit.getLogger().severe(StringUtils.translate(message));
    }
}
