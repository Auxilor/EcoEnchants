package com.willfp.ecoenchants.display.options;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.display.options.interfaces.ThresholdedOption;
import com.willfp.ecoenchants.display.options.interfaces.ToggleableOption;
import com.willfp.ecoenchants.display.options.interfaces.UpdateableOption;
import com.willfp.ecoenchants.util.StringUtils;

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
        threshold = ConfigManager.getConfig().getInt("lore.describe.before-lines");
        enabled = ConfigManager.getConfig().getBool("lore.describe.enabled");
        color = StringUtils.translate(ConfigManager.getLang().getString("description-color"));
    }

    public String getColor() {
        return color;
    }
}
