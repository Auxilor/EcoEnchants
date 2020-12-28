package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class HoneyArtifact extends Artifact {
    public HoneyArtifact() {
        super(
                "honey_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.FALLING_HONEY;
    }
}
