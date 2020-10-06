package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;
public final class CloudsArtifact extends Artifact {
    public CloudsArtifact() {
        super(
                "clouds_artifact",
                5.0,
                Particle.REDSTONE,
                new Particle.DustOptions(Color.AQUA, 1.0f)
        );
    }
}