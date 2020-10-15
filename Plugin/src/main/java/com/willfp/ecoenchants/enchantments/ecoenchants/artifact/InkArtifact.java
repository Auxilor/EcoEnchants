package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class InkArtifact extends Artifact {
    public InkArtifact() {
        super(
                "ink_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.SQUID_INK;
    }
}