package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class WaterArtifact extends Artifact {
    public WaterArtifact() {
        super(
                "water_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.DRIP_WATER;
    }
}
