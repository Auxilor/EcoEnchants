package com.willfp.ecoenchants.integrations.mcmmo.plugins;

import com.gmail.nossr50.events.fake.FakeEvent;
import com.willfp.ecoenchants.integrations.mcmmo.McmmoIntegration;
import org.bukkit.event.Event;

public class McmmoIntegrationImpl implements McmmoIntegration {
    @Override
    public boolean isFake(Event event) {
        return event instanceof FakeEvent;
    }

    @Override
    public String getPluginName() {
        return "mcMMO";
    }
}
