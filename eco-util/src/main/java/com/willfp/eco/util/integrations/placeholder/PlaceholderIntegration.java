package com.willfp.eco.util.integrations.placeholder;

import com.willfp.eco.util.integrations.Integration;
import org.bukkit.entity.Player;

/**
 * Interface for Placeholder integrations
 */
public interface PlaceholderIntegration extends Integration {
    /**
     * Register the integration with the specified plugin
     * Not to be confused with internal registration in {@link PlaceholderManager#addIntegration(PlaceholderIntegration)}
     */
    void registerIntegration();

    /**
     * Translate all the placeholders in a string with respect to a player
     *
     * @param text   The text to translate
     * @param player The player to translate with respect to
     * @return The string, translated
     */
    String translate(String text, Player player);
}
