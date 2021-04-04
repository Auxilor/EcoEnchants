package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class WarpedArtifact extends Artifact {
    public WarpedArtifact() {
        super(
                "warped_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.WARPED_SPORE;
    }
}
