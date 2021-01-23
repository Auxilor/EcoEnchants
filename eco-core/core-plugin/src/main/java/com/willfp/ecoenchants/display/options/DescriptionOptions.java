package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class DescriptionOptions extends PluginDependent {
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
     * Create new description options.
     *
     * @param plugin EcoEnchants.
     */
    public DescriptionOptions(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Update the options.
     */
    public void update() {
        threshold = this.getPlugin().getConfigYml().getInt("lore.describe.before-lines");
        enabled = this.getPlugin().getConfigYml().getBool("lore.describe.enabled");
        color = StringUtils.translate(this.getPlugin().getLangYml().getString("description-color"));
    }
}
