package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class MagmaArtifact extends Artifact {
    public MagmaArtifact() {
        super(
                "magma_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.LAVA;
    }
}
