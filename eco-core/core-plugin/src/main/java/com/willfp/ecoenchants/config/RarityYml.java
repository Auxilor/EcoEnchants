package com.willfp.ecoenchants.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.BaseConfig;
import com.willfp.eco.core.config.ConfigType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RarityYml extends BaseConfig {
    /**
     * Instantiate rarity.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public RarityYml(@NotNull final EcoPlugin plugin) {
        super("rarity", plugin, false, ConfigType.YAML);
    }

    /**
     * Get all rarity names.
     *
     * @return Set of all rarity names.
     */
    public List<String> getRarities() {
        return this.getSubsection("rarities").getKeys(false);
    }
}
