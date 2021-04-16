package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class InkArtifact extends Artifact {
    public InkArtifact() {
        super(
                "ink_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.SQUID_INK;
    }
}
