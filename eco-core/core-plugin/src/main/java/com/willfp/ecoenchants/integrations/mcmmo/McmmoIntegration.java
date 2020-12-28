package com.willfp.ecoenchants.integrations.mcmmo;

import com.willfp.eco.util.integrations.Integration;
import org.bukkit.event.Event;

public interface McmmoIntegration extends Integration {
    /**
     * @param event The event to check.
     * @return If the event is fake.
     * @see McmmoManager#isFake(Event)
     */
    boolean isFake(Event event);
}
