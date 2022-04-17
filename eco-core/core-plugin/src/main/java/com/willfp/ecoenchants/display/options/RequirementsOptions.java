package com.willfp.ecoenchants.display.options;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class RequirementsOptions extends PluginDependent<EcoPlugin> {
    /**
     * If numerals should be used.
     * <p>
     * If false then numbers will be used instead.
     */
    @Getter
    private String requirementColor;

    /**
     * Create new numbers options.
     *
     * @param plugin EcoEnchants.
     */
    public RequirementsOptions(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Update the options.
     */
    public void update() {
        requirementColor = this.getPlugin().getLangYml().getString("missing-requirements-format");
    }
}
