package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class LazyHealthFixListener extends PluginDependent<EcoPlugin> implements Listener {
    public LazyHealthFixListener(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onJoin(@NotNull final PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.getHealth() >= 19.0) {
            this.getPlugin().getScheduler().runLater(3, () -> player.setHealth(
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()
            ));
        }
    }
}
