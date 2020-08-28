package com.willfp.ecoenchants.events.naturalexpgainevent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerExpChangeEvent;

class NaturalExpGainBuilder {
    private LivingEntity victim = null;
    private boolean cancelled = false;
    private PlayerExpChangeEvent event;
    private Location loc;

    public NaturalExpGainBuilder() {

    }

    public LivingEntity getVictim() {
        return this.victim;
    }

    public void setEvent(PlayerExpChangeEvent event) {
        this.event = event;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public void setLoc(Location location) {
        this.loc = location;
    }

    public Location getLoc() {
        return this.loc;
    }

    public void push() {
        if(this.event == null) return;
        if(this.cancelled) return;

        NaturalExpGainEvent naturalExpGainEvent = new NaturalExpGainEvent(event);

        Bukkit.getPluginManager().callEvent(naturalExpGainEvent);
    }
}
