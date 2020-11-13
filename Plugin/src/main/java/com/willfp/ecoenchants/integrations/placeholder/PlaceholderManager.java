package com.willfp.ecoenchants.integrations.placeholder;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class PlaceholderManager {
    private static final Set<PlaceholderEntry> placeholders = new HashSet<>();

    public static void addIntegration(PlaceholderIntegration integration) {
        integration.registerIntegration();
    }

    public static void registerPlaceholder(PlaceholderEntry expansion) {
        placeholders.removeIf(placeholderEntry -> placeholderEntry.getIdentifier().equalsIgnoreCase(expansion.getIdentifier()));
        placeholders.add(expansion);
    }

    public static String getResult(Player player, String identifier) {
        Optional<PlaceholderEntry> matching = placeholders.stream().filter(expansion -> expansion.getIdentifier().equalsIgnoreCase(identifier)).findFirst();
        if(matching.isPresent()) {
            return matching.get().getResult(player);
        } else {
            return null;
        }
    }
}
