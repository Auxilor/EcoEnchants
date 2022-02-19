package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class SparkArtifact extends Artifact {
    public SparkArtifact() {
        super(
                "spark_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.ELECTRIC_SPARK;
    }
}
