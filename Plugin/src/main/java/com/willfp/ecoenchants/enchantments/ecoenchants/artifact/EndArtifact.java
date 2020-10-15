package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class EndArtifact extends Artifact {
    public EndArtifact() {
        super(
                "end_artifact",
                5.0
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.END_ROD;
    }
}