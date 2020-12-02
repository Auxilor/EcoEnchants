package com.willfp.ecoenchants.util.internal.updater;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (UpdateChecker.isOutdated()) {
            if (event.getPlayer().hasPermission("ecoenchants.updateannounce")) {
                event.getPlayer().sendMessage(ConfigManager.getLang().getMessage("outdated").replace("%ver%", EcoEnchantsPlugin.getInstance().getDescription().getVersion())
                        .replace("%newver%", UpdateChecker.getNewVersion()));
            }
        }
    }
}
