package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class MagmaArtifact extends Artifact {
    public MagmaArtifact() {
        super(
                "magma_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.LAVA;
    }
}