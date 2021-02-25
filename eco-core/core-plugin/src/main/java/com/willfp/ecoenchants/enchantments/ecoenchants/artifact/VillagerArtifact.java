package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class VillagerArtifact extends Artifact {
    public VillagerArtifact() {
        super(
                "villager_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.VILLAGER_HAPPY;
    }
}
