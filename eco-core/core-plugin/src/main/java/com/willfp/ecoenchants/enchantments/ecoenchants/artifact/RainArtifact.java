package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class RainArtifact extends Artifact {
    public RainArtifact() {
        super(
                "rain_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.WATER_SPLASH;
    }
}
