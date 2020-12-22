package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.config.Configs;
import com.willfp.ecoenchants.display.options.interfaces.ThresholdedOption;
import com.willfp.ecoenchants.display.options.interfaces.ToggleableOption;
import com.willfp.ecoenchants.display.options.interfaces.UpdateableOption;

public class DescriptionOptions implements ThresholdedOption, ToggleableOption, UpdateableOption {
    private int threshold;
    private boolean enabled;
    private String color;

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
        threshold = Configs.CONFIG.getInt("lore.describe.before-lines");
        enabled = Configs.CONFIG.getBool("lore.describe.enabled");
        color = StringUtils.translate(Configs.LANG.getString("description-color"));
    }

    public String getColor() {
        return color;
    }
}
