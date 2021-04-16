package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class SparkleArtifact extends Artifact {
    public SparkleArtifact() {
        super(
                "sparkle_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.FIREWORKS_SPARK;
    }
}
