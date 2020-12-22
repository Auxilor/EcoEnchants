package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class InkArtifact extends Artifact {
    public InkArtifact() {
        super(
                "ink_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.SQUID_INK;
    }
}