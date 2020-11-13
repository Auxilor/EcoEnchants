package com.willfp.ecoenchants.integrations.mcmmo;

import com.willfp.ecoenchants.integrations.Integration;
import org.bukkit.event.Event;

public interface McmmoIntegration extends Integration {
    boolean isFake(Event event);
}
