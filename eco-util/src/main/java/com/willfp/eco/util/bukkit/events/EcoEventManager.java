package com.willfp.eco.util.bukkit.events;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class EcoEventManager extends PluginDependent implements EventManager {
    public EcoEventManager(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this.plugin);
    }

    @Override
    public void unregisterEvents(Listener listener) {
        HandlerList.unregisterAll(listener);
    }

    @Override
    public void unregisterAllEvents() {
        HandlerList.unregisterAll(this.plugin);
    }
}
