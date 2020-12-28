package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.BaseConfig;

import java.util.Set;

public class Rarity extends BaseConfig {
    /**
     * Instantiate rarity.yml.
     */
    public Rarity() {
        super("rarity", false);
    }

    /**
     * Get all rarity names.
     *
     * @return Set of all rarity names.
     */
    public Set<String> getRarities() {
        return this.getConfig().getConfigurationSection("rarities").getKeys(false);
    }
}
