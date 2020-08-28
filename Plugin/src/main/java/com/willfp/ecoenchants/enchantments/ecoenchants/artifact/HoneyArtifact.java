package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class HoneyArtifact extends Artifact {
    public HoneyArtifact() {
        super(
                "honey_artifact",
                4.0,
                Particle.FALLING_HONEY
        );
    }
}