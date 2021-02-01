package com.willfp.ecoenchants.integrations.worldguard;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class WorldguardManager {
    /**
     * All registered WorldGuard integrations.
     */
    private static final Set<WorldguardWrapper> REGISTERED = new HashSet<>();

    /**
     * Register a new WorldGuard integration.
     *
     * @param worldguard The integration to register.
     */
    public static void register(@NotNull final WorldguardWrapper worldguard) {
        REGISTERED.add(worldguard);
    }

    /**
     * Register a new StateFlag with worldguard.
     *
     * @param flagName     The name of the flag.
     * @param defaultValue The default value for the flag to have.
     */
    public static void registerFlag(@NotNull final String flagName,
                                    final boolean defaultValue) {
        REGISTERED.forEach(worldguardWrapper -> worldguardWrapper.registerFlag(flagName, defaultValue));
    }

    /**
     * Get if an enchant is enabled at a player's location.
     *
     * @param enchant The enchantment to check.
     * @param player  The player to query.
     * @return If the enchantment is enabled at a player's location.
     */
    public static boolean enabledForPlayer(@NotNull final EcoEnchant enchant,
                                           @NotNull final LivingEntity player) {
        if (!(player instanceof Player)) {
            return true;
        }
        if (REGISTERED.isEmpty()) {
            return true;
        }

        for (WorldguardWrapper worldguardWrapper : REGISTERED) {
            if (worldguardWrapper.enabledForPlayer(enchant, (Player) player, player.getLocation())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get if an enchant is enabled at a specific location.
     *
     * @param enchant  The enchantment to check.
     * @param player   The player to query.
     * @param location The location to query.
     * @return If the enchantment is enabled at a player's location.
     */
    public static boolean enabledForPlayer(@NotNull final EcoEnchant enchant,
                                           @NotNull final LivingEntity player,
                                           @NotNull final Location location) {
        if (!(player instanceof Player)) {
            return true;
        }
        if (REGISTERED.isEmpty()) {
            return true;
        }

        for (WorldguardWrapper worldguardWrapper : REGISTERED) {
            if (worldguardWrapper.enabledForPlayer(enchant, (Player) player, location)) {
                return true;
            }
        }
        return false;
    }
}
