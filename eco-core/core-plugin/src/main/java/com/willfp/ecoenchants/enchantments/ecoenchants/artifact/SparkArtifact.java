package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.eco.core.Prerequisite;
import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class SparkArtifact extends Artifact {
    public SparkArtifact() {
        super(
                "spark_artifact",
                Prerequisite.HAS_1_17
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.ELECTRIC_SPARK;
    }
}
