package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

public class SparkleArtifact extends Artifact {
    public SparkleArtifact() {
        super(
                "sparkle_artifact",
                4.0,
                Particle.FIREWORKS_SPARK
        );
    }
}