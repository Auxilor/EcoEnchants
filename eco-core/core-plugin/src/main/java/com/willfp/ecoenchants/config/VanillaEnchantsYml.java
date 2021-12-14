package com.willfp.ecoenchants.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.BaseConfig;
import com.willfp.eco.core.config.ConfigType;
import org.jetbrains.annotations.NotNull;

public class VanillaEnchantsYml extends BaseConfig {
    /**
     * Instantiate target.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public VanillaEnchantsYml(@NotNull final EcoPlugin plugin) {
        super("vanillaenchants", plugin, true, ConfigType.YAML);
    }
}
