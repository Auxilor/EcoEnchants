package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;
public final class RedstoneArtifact extends Artifact {
    public RedstoneArtifact() {
        super(
                "redstone_artifact",
                5.0,
                Particle.REDSTONE,
                new Particle.DustOptions(Color.RED, 1.0f)
        );
    }
}