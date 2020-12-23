package com.willfp.eco.util.integrations.placeholder;

import com.willfp.eco.util.lambda.ObjectInputCallable;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderEntry {
    /**
     * The name of the placeholder, used in lookups.
     */
    @Getter
    private final String identifier;

    /**
     * The lambda to retrieve the output of the placeholder given a player.
     */
    private final ObjectInputCallable<String, Player> function;

    /**
     * If the placeholder requires a player to lookup.
     */
    private final boolean requiresPlayer;

    /**
     * Create a placeholder entry that doesn't require a player.
     *
     * @param identifier The identifier of the placeholder.
     * @param function   A lambda to get the result of the placeholder given a player.
     */
    public PlaceholderEntry(@NotNull final String identifier,
                            @NotNull final ObjectInputCallable<String, Player> function) {
        this(identifier, function, false);
    }

    /**
     * Create a placeholder entry that may require a player.
     *
     * @param identifier     The identifier of the placeholder.
     * @param function       A lambda to get the result of the placeholder.
     * @param requiresPlayer If the placeholder requires a player.
     */
    public PlaceholderEntry(@NotNull final String identifier,
                            @NotNull final ObjectInputCallable<String, Player> function,
                            final boolean requiresPlayer) {
        this.identifier = identifier;
        this.function = function;
        this.requiresPlayer = requiresPlayer;
    }

    /**
     * Get the result of the placeholder with respect to a player.
     *
     * @param player The player to translate with respect to.
     * @return The result of the placeholder.
     */
    public String getResult(@Nullable final Player player) {
        if (player == null) {
            Validate.isTrue(!requiresPlayer, "null player passed to requiresPlayer placeholder.");
        }
        return this.function.call(player);
    }

    /**
     * Get if the placeholder requires a player to get a result.
     *
     * @return If the placeholder requires a player.
     */
    public boolean requiresPlayer() {
        return requiresPlayer;
    }
}
