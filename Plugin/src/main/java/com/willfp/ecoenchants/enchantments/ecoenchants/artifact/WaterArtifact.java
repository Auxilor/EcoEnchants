package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class WaterArtifact extends Artifact {
    public WaterArtifact() {
        super(
                "water_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.DRIP_WATER;
    }
}