package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class HeartArtifact extends Artifact {
    public HeartArtifact() {
        super(
                "heart_artifact",
                5.0,
                Particle.HEART
        );
    }
}