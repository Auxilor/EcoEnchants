package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.BaseConfig;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.EcoEnchantsPlugin;

import java.util.Set;

public class Rarity extends BaseConfig {
    /**
     * Instantiate rarity.yml.
     */
    public Rarity() {
        super("rarity", false, EcoEnchantsPlugin.getInstance());
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
