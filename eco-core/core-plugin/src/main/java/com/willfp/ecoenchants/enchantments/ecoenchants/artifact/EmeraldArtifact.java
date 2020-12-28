package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class EmeraldArtifact extends Artifact {
    public EmeraldArtifact() {
        super(
                "emerald_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.COMPOSTER;
    }
}
