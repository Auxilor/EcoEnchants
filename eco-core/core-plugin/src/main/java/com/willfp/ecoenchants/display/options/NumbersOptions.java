package com.willfp.ecoenchants.display.options;

import com.willfp.ecoenchants.config.ConfigManager;
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
        useNumerals = ConfigManager.getConfig().getBool("lore.use-numerals");
        threshold = ConfigManager.getConfig().getInt("lore.use-numbers-above-threshold");
    }

    public boolean useNumerals() {
        return useNumerals;
    }
}
