package com.willfp.ecoenchants.config.configs;

import com.willfp.ecoenchants.config.UpdatingLang;
import com.willfp.ecoenchants.config.YamlConfig;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * Wrapper for lang.yml
 */
public class Lang extends UpdatingLang {
    public Lang() {
        super();
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStrings(String path) {
        return config.getStringList(path);
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