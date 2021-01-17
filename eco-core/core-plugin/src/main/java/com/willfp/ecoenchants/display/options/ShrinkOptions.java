package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import lombok.Getter;

public class ShrinkOptions {
    /**
     * Instance of EcoEnchants.
     */
    public static final AbstractEcoPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

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
        threshold = PLUGIN.getConfigYml().getInt("lore.shrink.after-lines");
        enabled = PLUGIN.getConfigYml().getBool("lore.shrink.enabled");
        shrinkPerLine = PLUGIN.getConfigYml().getInt("lore.shrink.maximum-per-line");
    }
}
