package com.willfp.ecoskills.config;

import com.willfp.eco.core.config.ExtendableConfig;
import com.willfp.ecoskills.EcoSkillsPlugin;
import org.jetbrains.annotations.NotNull;

public class ClassConfig extends ExtendableConfig {
    /**
     * Instantiate a new config for a skill class.
     *
     * @param name   The name of the config.
     * @param plugin The provider of the skill class.
     */
    public ClassConfig(@NotNull final String name,
                       @NotNull final Class<?> plugin) {
        super(name, true, EcoSkillsPlugin.getInstance(), plugin, "classes/");
    }
}
