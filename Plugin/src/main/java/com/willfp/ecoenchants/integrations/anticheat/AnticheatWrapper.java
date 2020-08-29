package com.willfp.ecoenchants.integrations.anticheat;

import com.willfp.ecoenchants.integrations.Integration;
import org.bukkit.entity.Player;

public interface AnticheatWrapper extends Integration {
    /**
     * Exempt a player from checks
     *
     * @param player The player to exempt
     */
    void exempt(Player player);

    /**
     * Unexempt a player from checks
     *
     * @param player The player to unexempt
     */
    void unexempt(Player player);
}
