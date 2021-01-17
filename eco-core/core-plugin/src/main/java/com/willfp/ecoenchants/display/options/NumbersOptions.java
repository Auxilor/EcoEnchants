package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import lombok.Getter;

public class NumbersOptions {
    /**
     * Instance of EcoEnchants.
     */
    public static final AbstractEcoPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

    /**
     * If numerals should be used.
     * <p>
     * If false then numbers will be used instead.
     */
    @Getter
    private boolean useNumerals;

    /**
     * The threshold above which numbers will be used instead.
     */
    @Getter
    private int threshold;

    /**
     * Update the options.
     */
    public void update() {
        useNumerals = PLUGIN.getConfigYml().getBool("lore.use-numerals");
        threshold = PLUGIN.getConfigYml().getInt("lore.use-numbers-above-threshold");
    }
}
