package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;
public class ZapArtifact extends Artifact {
    public ZapArtifact() {
        super(
                "zap_artifact"
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.REDSTONE;
    }

    @Override
    public Particle.DustOptions getDustOptions() {
        return new Particle.DustOptions(Color.YELLOW, 1.0f);
    }
}
