package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class WitchArtifact extends Artifact {
    public WitchArtifact() {
        super(
                "witch_artifact",
                5.0
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.SPELL_WITCH;
    }
}