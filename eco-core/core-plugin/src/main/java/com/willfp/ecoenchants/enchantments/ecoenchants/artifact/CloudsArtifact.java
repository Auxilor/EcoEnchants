package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class CloudsArtifact extends Artifact {
    public CloudsArtifact() {
        super(
                "clouds_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.REDSTONE;
    }

    @Override
    public Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.AQUA, 1.0f);
    }
}
