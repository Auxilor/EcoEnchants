package com.willfp.ecoenchants.integrations.anvilgui;

import com.willfp.ecoenchants.nms.OpenInventory;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class AnvilGUIManager {
    private static final Set<AnvilGUIIntegration> integrations = new HashSet<>();

    public static void registerIntegration(AnvilGUIIntegration integration) {
        integrations.add(integration);
    }

    public static boolean hasAnvilGUIOpen(Player player) {
        if(integrations.isEmpty())
            return false;
        return integrations.stream().anyMatch(integration -> integration.isInstance(OpenInventory.getOpenInventory(player)));
    }
}
