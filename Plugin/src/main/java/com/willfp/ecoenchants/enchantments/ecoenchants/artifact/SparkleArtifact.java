package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class SparkleArtifact extends Artifact {
    public SparkleArtifact() {
        super(
                "sparkle_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.FIREWORKS_SPARK;
    }
}