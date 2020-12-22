package com.willfp.ecoenchants.integrations.mcmmo;

import com.willfp.ecoenchants.integrations.Integration;
import org.bukkit.event.Event;

/**
 * Interface for mcMMO integrations
 */
public interface McmmoIntegration extends Integration {
    /**
     * @see McmmoManager#isFake(Event)
     */
    boolean isFake(Event event);
}
