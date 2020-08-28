package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class RedstoneArtifact extends Artifact {
    public RedstoneArtifact() {
        super(
                "redstone_artifact",
                4.0,
                Particle.REDSTONE,
                new Particle.DustOptions(Color.RED, 1.0f)
        );
    }
}