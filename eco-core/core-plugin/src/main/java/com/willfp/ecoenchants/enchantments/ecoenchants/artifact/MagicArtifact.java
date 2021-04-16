package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class MagicArtifact extends Artifact {
    public MagicArtifact() {
        super(
                "magic_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.CRIT_MAGIC;
    }
}
