package com.willfp.ecoenchants.sprintartifacts;

import com.willfp.eco.util.extensions.Extension;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class SprintArtifactsMain extends Extension {
    public SprintArtifactsMain(@NotNull final AbstractEcoPlugin plugin) {
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
