package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

public class FireArtifact extends Artifact {
    public FireArtifact() {
        super(
                "fire_artifact",
                4.0,
                Particle.FLAME
        );
    }
}