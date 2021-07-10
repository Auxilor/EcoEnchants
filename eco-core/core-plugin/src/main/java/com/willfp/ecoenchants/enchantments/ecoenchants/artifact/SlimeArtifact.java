package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class SlimeArtifact extends Artifact {
    public SlimeArtifact() {
        super(
                "slime_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.SLIME;
    }
}
