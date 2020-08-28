package com.willfp.ecoenchants.listeners;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.config.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (Main.outdated) {
            if (event.getPlayer().hasPermission("ecoenchants.updateannounce")) {
                event.getPlayer().sendMessage(ConfigManager.getLang().getMessage("outdated").replace("%ver%", Main.getInstance().getDescription().getVersion())
                        .replace("%newver%", Main.newVersion));
            }
        }
    }
}
