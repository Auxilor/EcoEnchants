package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class EndArtifact extends Artifact {
    public EndArtifact() {
        super(
                "end_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.END_ROD;
    }
}
