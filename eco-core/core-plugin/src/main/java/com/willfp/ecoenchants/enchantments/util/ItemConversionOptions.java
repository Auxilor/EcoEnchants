package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ItemConversionOptions {
    /**
     * Allow reading enchantments from lore-based plugins.
     */
    @Getter
    private boolean usingLoreGetter = false;

    /**
     * Allow reading enchantments from lore-based plugins aggressively.
     */
    @Getter
    private boolean usingAggressiveLoreGetter = false;

    /**
     * If the experimental hide fixer is being used.
     */
    @Getter
    private boolean usingExperimentalHideFixer = false;

    /**
     * If the aggressive experimental hide fixer is being used.
     */
    @Getter
    private boolean usingAggressiveExperimentalHideFixer = false;

    /**
     * If all items should have hide enchants removed.
     */
    @Getter
    private boolean usingForceHideFixer = false;

    /**
     * If above max level enchantments should be clamped.
     */
    @Getter
    private boolean usingLevelClamp = false;

    /**
     * If items with above max level enchantments should be deleted.
     */
    @Getter
    private boolean usingLevelClampDelete = false;

    /**
     * If illegal enchantments should be deleted.
     */
    @Getter
    private boolean removingIllegal = false;

    /**
     * If illegal items should be deleted.
     */
    @Getter
    private boolean deletingIllegal = false;

    /**
     * If hard cap clamp is enabled.
     */
    @Getter
    private boolean usingHardCapClamp = false;

    /**
     * If disabled enchantments should be removed entirely.
     */
    @Getter
    private boolean removeDisabled = false;

    /**
     * Reload the options.
     *
     * @param plugin Instance of ecoenchants.
     */
    @ConfigUpdater
    public void reload(@NotNull final EcoPlugin plugin) {
        usingLoreGetter = plugin.getConfigYml().getBool("advanced.lore-getter.enabled");
        usingAggressiveLoreGetter = plugin.getConfigYml().getBool("advanced.lore-getter.aggressive");
        usingExperimentalHideFixer = plugin.getConfigYml().getBool("advanced.hide-fixer.enabled");
        usingAggressiveExperimentalHideFixer = plugin.getConfigYml().getBool("advanced.hide-fixer.aggressive");
        usingForceHideFixer = plugin.getConfigYml().getBool("advanced.hide-fixer.force");
        usingLevelClamp = plugin.getConfigYml().getBool("advanced.level-clamp.enabled");
        usingLevelClampDelete = plugin.getConfigYml().getBool("advanced.level-clamp.delete-item");
        removingIllegal = plugin.getConfigYml().getBool("advanced.remove-illegal.enabled");
        deletingIllegal = plugin.getConfigYml().getBool("advanced.remove-illegal.delete-item");
        removeDisabled = plugin.getConfigYml().getBool("advanced.remove-disabled.enabled");
        usingHardCapClamp = plugin.getConfigYml().getBool("advanced.hard-cap-clamp.enabled");
    }

    static {
        reload(EcoEnchantsPlugin.getInstance());
    }
}
