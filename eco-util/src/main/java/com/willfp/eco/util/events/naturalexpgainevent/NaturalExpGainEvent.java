package com.willfp.eco.util.events.naturalexpgainevent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event triggered when player receives experience not from bottle
 */
public class NaturalExpGainEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The associated {@link PlayerExpChangeEvent}
     */
    private final PlayerExpChangeEvent event;

    /**
     * Create event based off parameters
     *
     * @param event The associate PlayerExpChangeEvent
     */
    public NaturalExpGainEvent(@NotNull PlayerExpChangeEvent event) {
        this.event = event;
    }

    /**
     * Get associated {@link PlayerExpChangeEvent}
     * Use this to modify event parameters.
     *
     * @return The associated {@link PlayerExpChangeEvent}
     */
    public PlayerExpChangeEvent getExpChangeEvent() {
        return this.event;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
