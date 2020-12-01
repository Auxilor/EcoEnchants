package com.willfp.ecoenchants.integrations.mcmmo.plugins;

import com.gmail.nossr50.events.fake.FakeEvent;
import com.willfp.ecoenchants.integrations.mcmmo.McmmoIntegration;
import com.willfp.ecoenchants.util.ClassUtils;
import org.bukkit.event.Event;

/**
 * Concrete implementation of {@link McmmoIntegration}
 */
public class McmmoIntegrationImpl implements McmmoIntegration {
    @Override
    public boolean isFake(Event event) {
        if(!ClassUtils.exists("com.gmail.nossr50.events.fake.FakeEvent"))
            return false;
        return event instanceof FakeEvent;
    }

    @Override
    public String getPluginName() {
        return "mcMMO";
    }
}
