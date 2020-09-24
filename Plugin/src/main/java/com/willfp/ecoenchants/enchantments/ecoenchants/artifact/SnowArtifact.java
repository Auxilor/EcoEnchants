package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class SnowArtifact extends Artifact {
    public SnowArtifact() {
        super(
                "snow_artifact",
                5.0,
                Particle.SNOWBALL
        );
    }
}