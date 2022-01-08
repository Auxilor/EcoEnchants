package com.willfp.ecoenchants.enchantments.custom;

import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomEcoEnchant extends EcoEnchant {
    /**
     * The levels.
     */
    private final Map<Integer, CustomEcoEnchantLevel> levels;

    /**
     * Create custom EcoEnchant.
     *
     * @param config The config.
     */
    public CustomEcoEnchant(@NotNull final Config config) {
        super(
                config.getString("id"), EnchantmentType.getByName(config.getString("type").toUpperCase()), config
        );

        this.levels = new HashMap<>();

        int i = 1;
        for (Config levelConfig : config.getSubsections("levels")) {
            levels.put(i, new CustomEcoEnchantLevel(this, levelConfig));
            i++;
        }
    }

    /**
     * Get the level.
     *
     * @param level The level.
     * @return The level.
     */
    public CustomEcoEnchantLevel getLevel(final int level) {
        return levels.get(level);
    }

    /**
     * Get the levels.
     *
     * @return The levels.
     */
    public Set<CustomEcoEnchantLevel> getLevels() {
        return new HashSet<>(levels.values());
    }

    @Override
    public int getMaxLevel() {
        return this.levels.size();
    }
}
