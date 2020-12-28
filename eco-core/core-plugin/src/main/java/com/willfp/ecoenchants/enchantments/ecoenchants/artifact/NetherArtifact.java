package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class NetherArtifact extends Artifact {
    public NetherArtifact() {
        super(
                "nether_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.PORTAL;
    }
}
