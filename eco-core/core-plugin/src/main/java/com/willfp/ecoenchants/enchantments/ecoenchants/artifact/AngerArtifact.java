package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class AngerArtifact extends Artifact {
    public AngerArtifact() {
        super(
                "anger_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.VILLAGER_ANGRY;
    }
}
