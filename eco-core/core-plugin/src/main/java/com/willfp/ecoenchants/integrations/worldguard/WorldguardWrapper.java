package com.willfp.ecoenchants.integrations.worldguard;

import com.willfp.eco.util.integrations.Integration;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WorldguardWrapper extends Integration {
    /**
     * Register a flag with WorldGuard.
     *
     * @param name The name of the flag.
     * @param def  The default value for the flag to have.
     */
    void registerFlag(String name, boolean def);

    /**
     * Get if an enchantment is available to be used at a specific location by a player.
     *
     * @param enchant  The enchantment to query.
     * @param player   The player to query.
     * @param location The location to query.
     * @return If the enchantment is allowed to be used.
     */
    boolean enabledForPlayer(EcoEnchant enchant, Player player, Location location);
}
