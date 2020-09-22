package com.willfp.ecoenchants.util;

import net.md_5.bungee.api.ChatColor;

public class StringUtils {
    public static String translate(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
