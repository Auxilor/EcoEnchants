package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;

public final class LimeArtifact extends Artifact {
    public LimeArtifact() {
        super(
                "lime_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.REDSTONE;
    }

    @Override
    protected Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.fromRGB(3, 252, 140), 1.0f);
    }
}