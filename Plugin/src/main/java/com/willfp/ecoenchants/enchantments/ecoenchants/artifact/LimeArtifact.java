package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;

public class LimeArtifact extends Artifact {
    public LimeArtifact() {
        super(
                "lime_artifact",
                5.0,
                Particle.REDSTONE,
                new Particle.DustOptions(Color.fromRGB(3, 252, 140), 1.0f)
        );
    }
}