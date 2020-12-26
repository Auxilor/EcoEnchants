package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class DustArtifact extends Artifact {
    public DustArtifact() {
        super(
                "dust_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.CRIT;
    }
}
