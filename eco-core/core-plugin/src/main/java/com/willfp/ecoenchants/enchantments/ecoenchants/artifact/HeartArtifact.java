package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class HeartArtifact extends Artifact {
    public HeartArtifact() {
        super(
                "heart_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.HEART;
    }
}
