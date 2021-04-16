package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class RedstoneArtifact extends Artifact {
    public RedstoneArtifact() {
        super(
                "redstone_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.REDSTONE;
    }

    @Override
    public Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.RED, 1.0f);
    }
}
