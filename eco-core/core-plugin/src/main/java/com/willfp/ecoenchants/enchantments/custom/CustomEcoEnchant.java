package com.willfp.ecoenchants.enchantments.custom;

import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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
            levels.put(i, new CustomEcoEnchantLevel(this, levelConfig, i));
            i++;
        }
    }

    @Override
    public String getPlaceholder(final int level) {
        return this.getLevel(level).getValuePlaceholder();
    }

    @Override
    protected void postUpdate() {
        for (EnchantmentTarget target : this.getTargets()) {
            if (target.getSlot() == null) {
                Logger logger = this.getPlugin().getLogger();
                logger.warning("");
                logger.warning("Problem with target " + target.getName() + "!");
                logger.warning("Go to target.yml and specify a slot for enchants to activate on!");
                logger.warning("Example:");
                logger.warning(target.getName() + ":");
                logger.warning("  - slot:hands");
                logger.warning("  - material1");
                logger.warning("  - material2");
                logger.warning("");
                logger.warning("Read the wiki to see available slots!");
                logger.warning("Custom Enchantment " + this.getKey().getKey() + " will not work until then");
                logger.warning("");
            }
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

    @Override
    public boolean isEnabled() {
        return true;
    }

    static {
        new ConditionInEcoEnchantWorld();
        new ConditionHasEcoEnchantRequirements();
    }
}
