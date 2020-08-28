package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class CloudsArtifact extends Artifact {
    public CloudsArtifact() {
        super(
                "clouds_artifact",
                4.0,
                Particle.REDSTONE,
                new Particle.DustOptions(Color.AQUA, 1.0f)
        );
    }
}