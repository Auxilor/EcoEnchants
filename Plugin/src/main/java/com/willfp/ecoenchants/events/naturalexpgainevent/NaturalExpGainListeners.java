package com.willfp.ecoenchants.events.naturalexpgainevent;

import com.willfp.ecoenchants.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class NaturalExpGainListeners implements Listener {

    Set<NaturalExpGainBuilder> events = new HashSet<>();

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        NaturalExpGainBuilder builtEvent = new NaturalExpGainBuilder();
        builtEvent.setEvent(event);

        AtomicBoolean isNatural = new AtomicBoolean(true);
        AtomicReference<NaturalExpGainBuilder> atomicBuiltEvent = new AtomicReference<>();

        Set<NaturalExpGainBuilder> eventsClone = new HashSet<>(events);
        eventsClone.forEach((builder) -> {
            if(builder.getLoc().getWorld().getNearbyEntities(builder.getLoc(), 7.25, 7.25, 7.25).contains(event.getPlayer())) {
                events.remove(builder);
                isNatural.set(false);
                atomicBuiltEvent.set(builder);
            }
        });

        if(isNatural.get()) {
            events.remove(atomicBuiltEvent.get());
            builtEvent.push();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                events.remove(builtEvent);
            }
        }.runTaskLater(Main.getInstance(), 1);
    }

    @EventHandler
    public void onExpBottle(ExpBottleEvent event) {
        NaturalExpGainBuilder builtEvent = new NaturalExpGainBuilder();
        builtEvent.setLoc(event.getEntity().getLocation());

        events.add(builtEvent);
    }
}
