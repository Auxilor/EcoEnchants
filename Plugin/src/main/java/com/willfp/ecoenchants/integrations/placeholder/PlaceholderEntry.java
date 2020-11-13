package com.willfp.ecoenchants.integrations.placeholder;

import com.willfp.ecoenchants.util.interfaces.ObjectCallable;
import org.bukkit.entity.Player;

public class PlaceholderEntry {
    private final String identifier;
    private final ObjectCallable<String, Player> function;
    private final boolean requiresPlayer;

    public PlaceholderEntry(String identifier, ObjectCallable<String, Player> function) {
        this(identifier, function, false);
    }

    public PlaceholderEntry(String identifier, ObjectCallable<String, Player> function, boolean requiresPlayer) {
        this.identifier = identifier;
        this.function = function;
        this.requiresPlayer = requiresPlayer;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getResult(Player player) {
        return this.function.call(player);
    }

    public boolean requiresPlayer() {
        return requiresPlayer;
    }
}
