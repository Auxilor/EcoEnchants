package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class EmeraldArtifact extends Artifact {
    public EmeraldArtifact() {
        super(
                "emerald_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.COMPOSTER;
    }
}