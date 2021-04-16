package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class SweepArtifact extends Artifact {
    public SweepArtifact() {
        super(
                "sweep_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.SWEEP_ATTACK;
    }
}
