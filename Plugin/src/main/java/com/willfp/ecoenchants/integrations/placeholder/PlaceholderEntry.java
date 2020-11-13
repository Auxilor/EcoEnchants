package com.willfp.ecoenchants.integrations.placeholder;

import com.willfp.ecoenchants.util.ObjectCallable;
import org.bukkit.entity.Player;

public class PlaceholderEntry {
    private final String identifier;
    private final ObjectCallable<String, Player> function;

    public PlaceholderEntry(String identifier, ObjectCallable<String, Player> function) {
        this.identifier = identifier;
        this.function = function;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getResult(Player player) {
        return this.function.call(player);
    }
}
