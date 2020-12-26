package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class DamageArtifact extends Artifact {
    public DamageArtifact() {
        super(
                "damage_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.DAMAGE_INDICATOR;
    }
}
