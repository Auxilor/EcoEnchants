package com.willfp.ecoenchants.display.options;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class NumbersOptions extends PluginDependent<EcoPlugin> {
    /**
     * If numerals should be used.
     * <p>
     * If false then numbers will be used instead.
     */
    @Getter
    private boolean useNumerals;

    /**
     * The threshold above which numbers will be used instead.
     */
    @Getter
    private int threshold;

    /**
     * Create new numbers options.
     *
     * @param plugin EcoEnchants.
     */
    public NumbersOptions(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Update the options.
     */
    public void update() {
        useNumerals = this.getPlugin().getConfigYml().getBool("lore.use-numerals");
        threshold = this.getPlugin().getConfigYml().getInt("lore.use-numbers-above-threshold");
    }
}
