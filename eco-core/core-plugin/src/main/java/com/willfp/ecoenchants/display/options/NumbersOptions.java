package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.config.Configs;
import com.willfp.ecoenchants.display.options.interfaces.ThresholdedOption;
import com.willfp.ecoenchants.display.options.interfaces.UpdateableOption;

public class NumbersOptions implements ThresholdedOption, UpdateableOption {
    private boolean useNumerals;
    private int threshold;

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    public void update() {
        useNumerals = Configs.CONFIG.getBool("lore.use-numerals");
        threshold = Configs.CONFIG.getInt("lore.use-numbers-above-threshold");
    }

    public boolean useNumerals() {
        return useNumerals;
    }
}
