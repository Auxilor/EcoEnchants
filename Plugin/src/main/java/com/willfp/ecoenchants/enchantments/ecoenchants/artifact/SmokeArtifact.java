package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class SmokeArtifact extends Artifact {
    public SmokeArtifact() {
        super(
                "smoke_artifact",
                4.0,
                Particle.CAMPFIRE_COSY_SMOKE
        );
    }
}