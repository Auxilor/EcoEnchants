package com.willfp.ecoenchants.integrations.placeholder;

import com.willfp.ecoenchants.util.interfaces.ObjectBiCallable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

/**
 * A placeholder entry consists of an identifier and an {@link ObjectBiCallable<String, Player>} to fetch the result
 */
public class PlaceholderEntry {
    private final String identifier;
    private final ObjectBiCallable<String, Player> function;
    private final boolean requiresPlayer;

    /**
     * Create a placeholder entry that doesn't require a player
     *
     * @param identifier The identifier of the placeholder
     * @param function   A lambda to get the result of the placeholder
     */
    public PlaceholderEntry(String identifier, ObjectBiCallable<String, Player> function) {
        this(identifier, function, false);
    }

    /**
     * Create a placeholder entry that may require a player
     *
     * @param identifier     The identifier of the placeholder
     * @param function       A lambda to get the result of the placeholder
     * @param requiresPlayer If the placeholder requires a player
     */
    public PlaceholderEntry(String identifier, ObjectBiCallable<String, Player> function, boolean requiresPlayer) {
        this.identifier = identifier;
        this.function = function;
        this.requiresPlayer = requiresPlayer;
    }

    /**
     * Get the identifier of the placeholder
     *
     * @return The identifier
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Get the result of the placeholder with respect to a player
     *
     * @param player The player to translate with respect to
     * @return The result of the placeholder
     */
    public String getResult(@Nullable Player player) {
        return this.function.call(player);
    }

    /**
     * Get if the placeholder requires a player to get a result
     *
     * @return If the placeholder requires a player
     */
    public boolean requiresPlayer() {
        return requiresPlayer;
    }
}
