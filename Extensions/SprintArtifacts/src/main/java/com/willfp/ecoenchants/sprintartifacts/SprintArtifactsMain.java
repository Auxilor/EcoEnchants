package com.willfp.ecoenchants.sprintartifacts;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.extensions.Extension;
import org.bukkit.Bukkit;

public class SprintArtifactsMain extends Extension {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SprintArtifactsListener(), this.plugin);
    }

    @Override
    public void onDisable() {

    }
}
