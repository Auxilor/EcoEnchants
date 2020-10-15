package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class MagicArtifact extends Artifact {
    public MagicArtifact() {
        super(
                "magic_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.CRIT_MAGIC;
    }
}