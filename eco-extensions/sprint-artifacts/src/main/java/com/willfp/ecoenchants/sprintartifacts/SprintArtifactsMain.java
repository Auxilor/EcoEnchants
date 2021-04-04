package com.willfp.ecoenchants.sprintartifacts;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.extensions.Extension;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class SprintArtifactsMain extends Extension {
    public SprintArtifactsMain(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SprintArtifactsListener(), this.getPlugin());
    }

    @Override
    public void onDisable() {
        // Handled by super
    }
}
