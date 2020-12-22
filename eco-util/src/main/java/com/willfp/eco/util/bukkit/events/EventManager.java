package com.willfp.eco.util.bukkit.events;

import org.bukkit.event.Listener;

public interface EventManager {
    void registerEvents(Listener listener);
    void unregisterEvents(Listener listener);
    void unregisterAllEvents();
}
