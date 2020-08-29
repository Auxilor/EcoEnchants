package com.willfp.ecoenchants.integrations.anticheat.plugins;

import com.willfp.ecoenchants.integrations.anticheat.AnticheatWrapper;
import me.vagdedes.spartan.api.PlayerViolationEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class AnticheatSpartan implements AnticheatWrapper, Listener {
    private final Set<UUID> exempt = new HashSet<>();

    @Override
    public String getPluginName() {
        return "Spartan";
    }

    @Override
    public void exempt(Player player) {
        this.exempt.add(player.getUniqueId());
    }

    @Override
    public void unexempt(Player player) {
        this.exempt.remove(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onViolate(PlayerViolationEvent event) {
        if(!exempt.contains(event.getPlayer().getUniqueId())) {
            return;
        }

        event.setCancelled(true);
    }
}
