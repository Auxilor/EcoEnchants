package com.willfp.ecoenchants.integrations.anvilgui;

import com.willfp.ecoenchants.nms.OpenInventory;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for interfacing with plugins that use WesJD's AnvilGUI library
 */
public class AnvilGUIManager {
    private static final Set<AnvilGUIIntegration> integrations = new HashSet<>();

    /**
     * Register a new AnvilGUI integration
     *
     * @param integration The integration to register
     */
    public static void registerIntegration(AnvilGUIIntegration integration) {
        integrations.add(integration);
    }

    /**
     * Get if a player's open inventory is an AnvilGUI
     *
     * @param player The player to check
     * @return If the player's open inventory is an AnvilGUI
     */
    public static boolean hasAnvilGUIOpen(Player player) {
        if (integrations.isEmpty())
            return false;
        return integrations.stream().anyMatch(integration -> integration.isInstance(OpenInventory.getOpenInventory(player)));
    }
}
