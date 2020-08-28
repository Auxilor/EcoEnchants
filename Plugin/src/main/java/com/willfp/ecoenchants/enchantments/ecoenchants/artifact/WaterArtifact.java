package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class WaterArtifact extends Artifact {
    public WaterArtifact() {
        super(
                "water_artifact",
                4.0,
                Particle.DRIP_WATER
        );
    }
}