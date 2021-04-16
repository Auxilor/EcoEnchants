package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class TearArtifact extends Artifact {
    public TearArtifact() {
        super(
                "tear_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.DRIPPING_OBSIDIAN_TEAR;
    }
}
