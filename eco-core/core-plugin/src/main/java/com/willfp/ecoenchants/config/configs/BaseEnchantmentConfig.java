package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.config.ExtendableConfig;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.jetbrains.annotations.NotNull;

public class BaseEnchantmentConfig extends ExtendableConfig {
    /**
     * Instantiate a new config for an enchantment.
     *
     * @param name    The name of the config.
     * @param source  The class in the jar where the config is contained.
     * @param plugin  The provider of the enchantment.
     * @param enchant The enchantment.
     */
    public BaseEnchantmentConfig(@NotNull final String name,
                                 @NotNull final Class<?> source,
                                 @NotNull final EcoEnchant enchant,
                                 @NotNull final EcoPlugin plugin) {
        super(name, true, plugin, source, "enchants/" + enchant.getType().getName() + "/", ConfigType.YAML);
    }
}
