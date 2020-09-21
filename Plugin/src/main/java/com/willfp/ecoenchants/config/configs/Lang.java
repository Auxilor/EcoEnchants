package com.willfp.ecoenchants.config.configs;

import com.willfp.ecoenchants.config.RootConfig;
import org.bukkit.ChatColor;

/**
 * Wrapper for lang.yml
 */
public class Lang extends RootConfig {
    public Lang() {
        super("lang");
    }


    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("messages.prefix"));
    }

    public String getNoPermission() {
        return getPrefix() + ChatColor.translateAlternateColorCodes('&', config.getString("messages.no-permission"));
    }

    public String getMessage(String message) {
        return getPrefix() + ChatColor.translateAlternateColorCodes('&', config.getString("messages." + message));
    }
}