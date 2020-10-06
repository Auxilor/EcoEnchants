package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;
public final class ZapArtifact extends Artifact {
    public ZapArtifact() {
        super(
                "zap_artifact",
                5.0,
                Particle.REDSTONE,
                new Particle.DustOptions(Color.YELLOW, 1.0f)
        );
    }
}