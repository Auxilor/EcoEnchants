package com.willfp.eco.util.events.naturalexpgainevent;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.jetbrains.annotations.NotNull;

class NaturalExpGainBuilder {
    /**
     * If the event has been cancelled and no experience should be given.
     */
    @Getter
    @Setter
    private boolean cancelled = false;

    /**
     * The linked {@link PlayerExpChangeEvent}.
     */
    @Getter
    @Setter
    private PlayerExpChangeEvent event;

    /**
     * The location of the event.
     */
    @Getter
    @Setter
    private Location location;

    /**
     * The reason why the event was built.
     */
    @Getter
    @Setter
    private BuildReason reason;

    /**
     * Build a new {@link NaturalExpGainEvent} given a specific reason.
     *
     * @param reason The {@link BuildReason}.
     */
    NaturalExpGainBuilder(@NotNull final BuildReason reason) {
        this.reason = reason;
    }

    /**
     * Call the event on the server.
     */
    public void push() {
        Validate.notNull(event);
        if (this.cancelled) {
            return;
        }

        NaturalExpGainEvent naturalExpGainEvent = new NaturalExpGainEvent(event);

        Bukkit.getPluginManager().callEvent(naturalExpGainEvent);
    }

    public enum BuildReason {
        /**
         * If the event was triggered by an experience bottle.
         */
        BOTTLE,

        /**
         * If the event was triggered by a natural experience change.
         */
        PLAYER
    }
}
