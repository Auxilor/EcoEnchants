package com.willfp.ecoenchants.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.BaseConfig;
import com.willfp.eco.core.config.ConfigType;
import org.jetbrains.annotations.NotNull;

public class CustomEnchantsYml extends BaseConfig {
    /**
     * Instantiate target.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public CustomEnchantsYml(@NotNull final EcoPlugin plugin) {
        super("customenchants", plugin, true, ConfigType.YAML);
    }
}
