package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class DamageArtifact extends Artifact {
    public DamageArtifact() {
        super(
                "damage_artifact",
                5.0
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.DAMAGE_INDICATOR;
    }
}