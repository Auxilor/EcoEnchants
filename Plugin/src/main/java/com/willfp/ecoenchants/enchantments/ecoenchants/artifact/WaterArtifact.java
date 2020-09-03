package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class WaterArtifact extends Artifact {
    public WaterArtifact() {
        super(
                "water_artifact",
                5.0,
                Particle.DRIP_WATER
        );
    }
}