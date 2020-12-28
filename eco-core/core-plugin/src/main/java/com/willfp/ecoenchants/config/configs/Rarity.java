package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.BaseConfig;

import java.util.Set;

/**
 * Wrapper for config.yml
 */
public class Rarity extends BaseConfig {
    public Rarity() {
        super("rarity", false);
    }

    public Set<String> getRarities() {
        return this.getConfig().getConfigurationSection("rarities").getKeys(false);
    }
}
