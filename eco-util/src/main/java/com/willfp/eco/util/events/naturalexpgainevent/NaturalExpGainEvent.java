package com.willfp.eco.util.events.naturalexpgainevent;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.jetbrains.annotations.NotNull;

public class NaturalExpGainEvent extends Event {
    /**
     * Internal bukkit.
     */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The associated {@link PlayerExpChangeEvent}.
     * Use this to modify event parameters.
     */
    @Getter
    private final PlayerExpChangeEvent expChangeEvent;

    /**
     * Create event based off parameters.
     *
     * @param event The associated PlayerExpChangeEvent.
     */
    public NaturalExpGainEvent(@NotNull final PlayerExpChangeEvent event) {
        this.expChangeEvent = event;
    }

    /**
     * Internal bukkit.
     *
     * @return The handlers.
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Internal bukkit.
     *
     * @return The handlers.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
