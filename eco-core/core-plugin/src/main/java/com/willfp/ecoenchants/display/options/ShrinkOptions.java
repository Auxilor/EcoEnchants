package com.willfp.ecoenchants.display.options;

import com.willfp.ecoenchants.config.ConfigManager;
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
        threshold = ConfigManager.getConfig().getInt("lore.shrink.after-lines");
        enabled = ConfigManager.getConfig().getBool("lore.shrink.enabled");
        shrinkPerLine = ConfigManager.getConfig().getInt("lore.shrink.maximum-per-line");
    }

    public int getShrinkPerLine() {
        return shrinkPerLine;
    }
}
