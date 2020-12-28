package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.config.Configs;
import lombok.Getter;

public class NumbersOptions {
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
        useNumerals = Configs.CONFIG.getBool("lore.use-numerals");
        threshold = Configs.CONFIG.getInt("lore.use-numbers-above-threshold");
    }
}
