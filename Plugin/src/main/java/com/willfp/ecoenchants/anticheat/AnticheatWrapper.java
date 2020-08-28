package com.willfp.ecoenchants.anticheat;

import org.bukkit.entity.Player;

public interface AnticheatWrapper {
    /**
     * Get the name of anticheat
     * @return The name
     */
    String getPluginName();

    /**
     * Exempt a player from checks
     * @param player The player to exempt
     */
    void exempt(Player player);

    /**
     * Unexempt a player from checks
     * @param player The player to unexempt
     */
    void unexempt(Player player);
}
