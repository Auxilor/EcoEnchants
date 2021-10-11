package com.willfp.ecoenchants.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.yaml.YamlBaseConfig;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DataYml extends YamlBaseConfig {

    /**
     * Instantiate data.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public DataYml(@NotNull final EcoPlugin plugin) {
        super("data", false, plugin);
    }

    /**
     * Get descriptions state (enabled/disabled) for the given player.
     *
     * @param player A player to get the descriptions state of.
     * @return Descriptions state for the given player.
     */
    public boolean isDescriptionEnabled(@NotNull final Player player) {
        if (this.getBoolOrNull(player.getUniqueId() + ".describe") == null) return true;
        return this.getBool(player.getUniqueId() + ".describe");
    }

    /**
     * Set descriptions state (enabled/disabled) for the given player.
     *
     * @param player A player to set the given state for.
     * @param enabled The state to set for the given player.
     */
    public void setDescriptionEnabled(@NotNull final Player player, final boolean enabled) {
        this.set(player.getUniqueId() + ".describe", enabled);
    }

    /**
     * Toggle descriptions state (enabled->disabled | disabled->enabled) for the given player.
     *
     * @param player A player to toggle the state for.
     */
    public void toggleDescriptions(@NotNull final Player player) {
        setDescriptionEnabled(player, !isDescriptionEnabled(player));
    }

}
