package com.willfp.eco.util.config.configs;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.config.BaseConfig;

import java.util.List;

/**
 * Wrapper for lang.yml
 */
public class Lang extends BaseConfig {
    public Lang() {
        super("lang", false);
    }

    public String getString(String path) {
        return StringUtils.translate(String.valueOf(config.getString(path)));
    }

    public List<String> getStrings(String path) {
        return config.getStringList(path);
    }


    public String getPrefix() {
        return StringUtils.translate(config.getString("messages.prefix"));
    }

    public String getNoPermission() {
        return getPrefix() + StringUtils.translate(config.getString("messages.no-permission"));
    }

    public String getMessage(String message) {
        return getPrefix() + StringUtils.translate(config.getString("messages." + message));
    }
}