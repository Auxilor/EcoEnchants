package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class NetherArtifact extends Artifact {
    public NetherArtifact() {
        super(
                "nether_artifact",
                4.0,
                Particle.PORTAL
        );
    }
}