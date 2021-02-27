package com.willfp.ecoenchants.config;

import com.willfp.eco.util.config.ExtendableConfig;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.jetbrains.annotations.NotNull;

public abstract class EnchantmentYamlConfig extends ExtendableConfig {
    /**
     * Create new enchantment config yml.
     *
     * @param name   The config name.
     * @param source The class of the main class of source or extension.
     * @param type   The enchantment type.
     */
    protected EnchantmentYamlConfig(@NotNull final String name,
                                    @NotNull final Class<?> source,
                                    @NotNull final EnchantmentType type) {
        super(name, true, EcoEnchantsPlugin.getInstance(), source, "enchants/" + type.getName() + "/");
    }
}
