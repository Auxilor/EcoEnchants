package com.willfp.ecoenchants.integrations.placeholder;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class PlaceholderManager {
    private static final Set<PlaceholderEntry> placeholders = new HashSet<>();
    private static final Set<PlaceholderIntegration> integrations = new HashSet<>();

    public static void addIntegration(PlaceholderIntegration integration) {
        integration.registerIntegration();
        integrations.add(integration);
    }

    public static void registerPlaceholder(PlaceholderEntry expansion) {
        placeholders.removeIf(placeholderEntry -> placeholderEntry.getIdentifier().equalsIgnoreCase(expansion.getIdentifier()));
        placeholders.add(expansion);
    }

    public static String getResult(Player player, String identifier) {
        Optional<PlaceholderEntry> matching = placeholders.stream().filter(expansion -> expansion.getIdentifier().equalsIgnoreCase(identifier)).findFirst();
        return matching.map(placeholderEntry -> placeholderEntry.getResult(player)).orElse(null);
    }

    public static String translatePlaceholders(String text, Player player) {
        AtomicReference<String> translatedReference = new AtomicReference<>(text);
        integrations.forEach(placeholderIntegration -> translatedReference.set(placeholderIntegration.translate(translatedReference.get(), player)));
        return translatedReference.get();
    }
}
