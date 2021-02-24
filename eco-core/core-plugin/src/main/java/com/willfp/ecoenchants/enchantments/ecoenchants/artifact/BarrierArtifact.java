package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class BarrierArtifact extends Artifact {
    public BarrierArtifact() {
        super(
                "barrier_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.BARRIER;
    }
}
