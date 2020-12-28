package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class MagmaArtifact extends Artifact {
    public MagmaArtifact() {
        super(
                "magma_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.LAVA;
    }
}
