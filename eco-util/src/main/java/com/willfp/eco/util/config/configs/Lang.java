package com.willfp.eco.util.config.configs;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.config.BaseConfig;
import org.jetbrains.annotations.NotNull;

/**
 * Wrapper for lang.yml
 */
public class Lang extends BaseConfig {
    public Lang() {
        super("lang", false);
    }

    public String getPrefix() {
        return StringUtils.translate(this.getConfig().getString("messages.prefix"));
    }

    public String getNoPermission() {
        return getPrefix() + StringUtils.translate(this.getConfig().getString("messages.no-permission"));
    }

    public String getMessage(@NotNull final String message) {
        return getPrefix() + StringUtils.translate(this.getConfig().getString("messages." + message));
    }
}
