package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class CopperArtifact extends Artifact {
    public CopperArtifact() {
        super(
                "copper_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.WAX_ON;
    }
}
