package com.willfp.ecoenchants.integrations.mcmmo;

import com.willfp.eco.util.ClassUtils;
import org.bukkit.event.Event;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility class for interfacing with mcMMO
 */
public class McmmoManager {
    private static final Set<McmmoIntegration> integrations = new HashSet<>();

    /**
     * Register a new mcMMO integration
     *
     * @param integration The integration to register
     */
    public static void registerIntegration(McmmoIntegration integration) {
        if(!ClassUtils.exists("com.gmail.nossr50.events.fake.FakeEvent"))
            return;
        integrations.add(integration);
    }

    /**
     * Get if an event is fake
     *
     * @param event The event to check
     * @return If the event is fake
     */
    public static boolean isFake(Event event) {
        AtomicBoolean isFake = new AtomicBoolean(false);
        integrations.forEach(integration -> {
            if (integration.isFake(event)) isFake.set(true);
        });

        return isFake.get();
    }
}
