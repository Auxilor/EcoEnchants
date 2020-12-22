package com.willfp.eco.util.integrations.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Utility class for placeholders
 */
public class PlaceholderManager {
    private static final Set<PlaceholderEntry> placeholders = new HashSet<>();
    private static final Set<PlaceholderIntegration> integrations = new HashSet<>();

    /**
     * Register a new placeholder integration
     *
     * @param integration The {@link PlaceholderIntegration} to register
     */
    public static void addIntegration(PlaceholderIntegration integration) {
        integration.registerIntegration();
        integrations.add(integration);
    }

    /**
     * Register a placeholder
     *
     * @param expansion The {@link PlaceholderEntry} to register
     */
    public static void registerPlaceholder(PlaceholderEntry expansion) {
        placeholders.removeIf(placeholderEntry -> placeholderEntry.getIdentifier().equalsIgnoreCase(expansion.getIdentifier()));
        placeholders.add(expansion);
    }

    /**
     * Get the result of a placeholder with respect to a player
     *
     * @param player     The player to get the result from
     * @param identifier The placeholder identifier
     * @return The value of the placeholder
     */
    public static String getResult(@Nullable Player player, String identifier) {
        Optional<PlaceholderEntry> matching = placeholders.stream().filter(expansion -> expansion.getIdentifier().equalsIgnoreCase(identifier)).findFirst();
        if (!matching.isPresent())
            return null;
        PlaceholderEntry entry = matching.get();
        if (player == null && entry.requiresPlayer())
            return "";
        return entry.getResult(player);
    }

    /**
     * Translate all placeholders with respect to a player
     *
     * @param text   The text that may contain placeholders to translate
     * @param player The player to translate the placeholders with respect to
     * @return The text, translated
     */
    public static String translatePlaceholders(String text, @Nullable Player player) {
        AtomicReference<String> translatedReference = new AtomicReference<>(text);
        integrations.forEach(placeholderIntegration -> translatedReference.set(placeholderIntegration.translate(translatedReference.get(), player)));
        return translatedReference.get();
    }
}
