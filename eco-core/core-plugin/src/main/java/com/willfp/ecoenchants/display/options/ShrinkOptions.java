package com.willfp.ecoenchants.display.options;

import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ShrinkOptions extends PluginDependent {
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
     * Create new shrink options.
     *
     * @param plugin EcoEnchants.
     */
    public ShrinkOptions(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Update the options.
     */
    public void update() {
        threshold = this.getPlugin().getConfigYml().getInt("lore.shrink.after-lines");
        enabled = this.getPlugin().getConfigYml().getBool("lore.shrink.enabled");
        shrinkPerLine = this.getPlugin().getConfigYml().getInt("lore.shrink.maximum-per-line");
    }
}
