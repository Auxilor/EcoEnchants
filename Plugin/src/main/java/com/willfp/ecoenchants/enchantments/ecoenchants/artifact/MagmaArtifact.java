package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class MagmaArtifact extends Artifact {
    public MagmaArtifact() {
        super(
                "magma_artifact",
                4.0,
                Particle.LAVA
        );
    }
}