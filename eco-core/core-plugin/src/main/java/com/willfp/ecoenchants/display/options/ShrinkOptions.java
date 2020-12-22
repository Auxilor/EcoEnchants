package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.config.Configs;
import com.willfp.ecoenchants.display.options.interfaces.ThresholdedOption;
import com.willfp.ecoenchants.display.options.interfaces.ToggleableOption;
import com.willfp.ecoenchants.display.options.interfaces.UpdateableOption;

public class ShrinkOptions implements ThresholdedOption, ToggleableOption, UpdateableOption {
    private int threshold;
    private boolean enabled;
    private int shrinkPerLine;

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void update() {
        threshold = Configs.CONFIG.getInt("lore.shrink.after-lines");
        enabled = Configs.CONFIG.getBool("lore.shrink.enabled");
        shrinkPerLine = Configs.CONFIG.getInt("lore.shrink.maximum-per-line");
    }

    public int getShrinkPerLine() {
        return shrinkPerLine;
    }
}
