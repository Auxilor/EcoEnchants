package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.config.Configs;
import com.willfp.ecoenchants.display.options.interfaces.ThresholdedOption;
import com.willfp.ecoenchants.display.options.interfaces.ToggleableOption;
import com.willfp.ecoenchants.display.options.interfaces.UpdateableOption;
import lombok.Getter;

public class DescriptionOptions implements ThresholdedOption, ToggleableOption, UpdateableOption {
    /**
     * The threshold below which to describe enchantments.
     */
    @Getter
    private int threshold;

    /**
     * If the options are enabled.
     */
    @Getter
    private boolean enabled;

    /**
     * The description lines color.
     */
    @Getter
    private String color;

    /**
     * Update the options.
     */
    @Override
    public void update() {
        threshold = Configs.CONFIG.getInt("lore.describe.before-lines");
        enabled = Configs.CONFIG.getBool("lore.describe.enabled");
        color = StringUtils.translate(Configs.LANG.getString("description-color"));
    }
}
