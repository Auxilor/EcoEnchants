package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class NetherArtifact extends Artifact {
    public NetherArtifact() {
        super(
                "nether_artifact",
                5.0,
                Particle.PORTAL
        );
    }
}