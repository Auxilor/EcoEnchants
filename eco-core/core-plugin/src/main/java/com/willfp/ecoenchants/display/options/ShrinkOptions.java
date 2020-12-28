package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.config.Configs;
import lombok.Getter;

public class ShrinkOptions {
    /**
     * The threshold above which enchantments will be shrunk.
     */
    @Getter
    private int threshold;

    /**
     * If shrinking is enabled.
     */
    @Getter
    private boolean enabled;

    /**
     * The amount of enchantments to have per-line.
     */
    @Getter
    private int shrinkPerLine;

    /**
     * Update the options.
     */
    public void update() {
        threshold = Configs.CONFIG.getInt("lore.shrink.after-lines");
        enabled = Configs.CONFIG.getBool("lore.shrink.enabled");
        shrinkPerLine = Configs.CONFIG.getInt("lore.shrink.maximum-per-line");
    }
}
