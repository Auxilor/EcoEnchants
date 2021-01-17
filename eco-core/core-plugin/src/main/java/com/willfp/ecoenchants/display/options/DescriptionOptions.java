package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import lombok.Getter;

public class DescriptionOptions {
    /**
     * Instance of EcoEnchants.
     */
    public static final AbstractEcoPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

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
    public void update() {
        threshold = PLUGIN.getConfigYml().getInt("lore.describe.before-lines");
        enabled = PLUGIN.getConfigYml().getBool("lore.describe.enabled");
        color = StringUtils.translate(PLUGIN.getLangYml().getString("description-color"));
    }
}
