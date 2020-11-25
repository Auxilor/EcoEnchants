package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class WitchArtifact extends Artifact {
    public WitchArtifact() {
        super(
                "witch_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.SPELL_WITCH;
    }
}