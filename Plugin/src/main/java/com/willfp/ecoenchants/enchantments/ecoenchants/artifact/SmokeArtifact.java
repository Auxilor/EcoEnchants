package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class SmokeArtifact extends Artifact {
    public SmokeArtifact() {
        super(
                "smoke_artifact",
                5.0
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.CAMPFIRE_COSY_SMOKE;
    }
}