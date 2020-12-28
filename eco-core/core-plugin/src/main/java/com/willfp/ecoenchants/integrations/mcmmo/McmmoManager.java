package com.willfp.ecoenchants.integrations.mcmmo;

import com.willfp.eco.util.ClassUtils;
import lombok.experimental.UtilityClass;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility class for interfacing with mcMMO
 */
@UtilityClass
public class McmmoManager {
    private static final Set<McmmoIntegration> REGISTERED = new HashSet<>();

    /**
     * Register a new mcMMO integration
     *
     * @param integration The integration to register
     */
    public static void registerIntegration(@NotNull final McmmoIntegration integration) {
        if (!ClassUtils.exists("com.gmail.nossr50.events.fake.FakeEvent")) {
            return;
        }
        REGISTERED.add(integration);
    }

    /**
     * Get if an event is fake
     *
     * @param event The event to check
     * @return If the event is fake
     */
    public static boolean isFake(@NotNull final Event event) {
        AtomicBoolean isFake = new AtomicBoolean(false);
        REGISTERED.forEach(integration -> {
            if (integration.isFake(event)) {
                isFake.set(true);
            }
        });

        return isFake.get();
    }
}
