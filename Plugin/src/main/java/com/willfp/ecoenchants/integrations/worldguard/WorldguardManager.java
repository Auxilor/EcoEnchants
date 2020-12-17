package com.willfp.ecoenchants.integrations.worldguard;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class WorldguardManager {
    private static final Set<WorldguardWrapper> worldguardWrappers = new HashSet<>();

    /**
     * Register a new WorldGuard integration
     *
     * @param worldguard The integration to register
     */
    public static void register(WorldguardWrapper worldguard) {
        worldguardWrappers.add(worldguard);
    }

    /**
     * Register a new StateFlag with worldguard
     *
     * @param flagName     The name of the flag
     * @param defaultValue The default value for the flag to have
     */
    public static void registerFlag(String flagName, boolean defaultValue) {
        worldguardWrappers.forEach(worldguardWrapper -> worldguardWrapper.registerFlag(flagName, defaultValue));
    }

    /**
     * Get if an enchant is enabled at a player's location
     *
     * @param enchant The enchantment to check
     * @param player  The player to query
     * @return If the enchantment is enabled at a player's location
     */
    public static boolean enabledForPlayer(EcoEnchant enchant, LivingEntity player) {
        if (!(player instanceof Player)) return true;
        if(worldguardWrappers.isEmpty()) return true;
        return worldguardWrappers.stream().anyMatch(worldguardWrapper -> worldguardWrapper.enabledForPlayer(enchant, (Player) player, player.getLocation()));
    }

    /**
     * Get if an enchant is enabled at a specific location
     *
     * @param enchant  The enchantment to check
     * @param player   The player to query
     * @param location The location to query
     * @return If the enchantment is enabled at a player's location
     */
    public static boolean enabledForPlayer(EcoEnchant enchant, LivingEntity player, Location location) {
        if (!(player instanceof Player)) return true;
        if(worldguardWrappers.isEmpty()) return true;
        return worldguardWrappers.stream().anyMatch(worldguardWrapper -> worldguardWrapper.enabledForPlayer(enchant, (Player) player, location));
    }
}
