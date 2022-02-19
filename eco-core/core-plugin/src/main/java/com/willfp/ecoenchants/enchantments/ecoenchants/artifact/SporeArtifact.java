package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class SporeArtifact extends Artifact {
    public SporeArtifact() {
        super(
                "spore_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.FALLING_SPORE_BLOSSOM;
    }
}
