package com.willfp.ecoenchants.sprintartifacts;

import com.willfp.eco.util.extensions.Extension;
import org.bukkit.Bukkit;

public class SprintArtifactsMain extends Extension {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SprintArtifactsListener(), this.getPlugin());
    }

    @Override
    public void onDisable() {
        // Handled by super
    }
}
