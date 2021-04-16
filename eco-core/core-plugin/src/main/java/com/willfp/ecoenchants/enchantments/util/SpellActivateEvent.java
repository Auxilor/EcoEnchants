package com.willfp.ecoenchants.enchantments.util;

import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class SpellActivateEvent extends PlayerEvent implements Cancellable {
    /**
     * Internal, for bukkit.
     */
    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * The spell that was activated.
     */
    @Getter
    private final Spell spell;

    /**
     * If the spell activation was cancelled.
     */
    private boolean cancelled;

    /**
     * Create new SpellActivateEvent.
     *
     * @param player The player.
     * @param spell  The spell.
     */
    public SpellActivateEvent(@NotNull final Player player,
                              @NotNull final Spell spell) {
        super(player);

        this.spell = spell;
        this.cancelled = false;
    }

    /**
     * Bukkit parity.
     *
     * @return The handler list.
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
