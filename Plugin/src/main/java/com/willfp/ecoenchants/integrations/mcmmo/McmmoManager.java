package com.willfp.ecoenchants.integrations.mcmmo;

import org.bukkit.event.Event;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class McmmoManager {
    private static final Set<McmmoIntegration> integrations = new HashSet<>();

    public static void registerIntegration(McmmoIntegration integration) {
        integrations.add(integration);
    }

    public static boolean isFake(Event event) {
        AtomicBoolean isFake = new AtomicBoolean(false);
        integrations.forEach(integration -> {
            if(integration.isFake(event)) isFake.set(true);
        });

        return isFake.get();
    }
}
