package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class MagmaArtifact extends Artifact {
    public MagmaArtifact() {
        super(
                "magma_artifact",
                5.0,
                Particle.LAVA
        );
    }
}