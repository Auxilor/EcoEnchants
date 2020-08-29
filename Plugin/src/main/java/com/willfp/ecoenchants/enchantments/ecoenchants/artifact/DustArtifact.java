package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

public class DustArtifact extends Artifact {
    public DustArtifact() {
        super(
                "dust_artifact",
                4.0,
                Particle.CRIT
        );
    }
}