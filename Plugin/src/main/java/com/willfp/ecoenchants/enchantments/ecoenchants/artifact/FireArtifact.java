package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class FireArtifact extends Artifact {
    public FireArtifact() {
        super(
                "fire_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.FLAME;
    }
}