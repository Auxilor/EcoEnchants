package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class SmokeArtifact extends Artifact {
    public SmokeArtifact() {
        super(
                "smoke_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.CAMPFIRE_COSY_SMOKE;
    }
}
