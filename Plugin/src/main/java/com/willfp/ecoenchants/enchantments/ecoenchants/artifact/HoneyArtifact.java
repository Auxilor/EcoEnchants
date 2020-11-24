package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class HoneyArtifact extends Artifact {
    public HoneyArtifact() {
        super(
                "honey_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.FALLING_HONEY;
    }
}