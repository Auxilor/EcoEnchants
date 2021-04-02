package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.BaseConfig;
import com.willfp.ecoenchants.EcoEnchantsPlugin;

import java.util.List;

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
    public List<String> getRarities() {
        return this.getSubsection("rarities").getKeys(false);
    }
}
