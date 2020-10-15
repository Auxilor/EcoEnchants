package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;
public final class CloudsArtifact extends Artifact {
    public CloudsArtifact() {
        super(
                "clouds_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.REDSTONE;
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.AQUA, 1.0f);
    }
}