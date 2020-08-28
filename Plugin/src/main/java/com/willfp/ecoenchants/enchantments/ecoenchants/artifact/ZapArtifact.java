package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Color;
import org.bukkit.Particle;
public class ZapArtifact extends Artifact {
    public ZapArtifact() {
        super(
                "zap_artifact",
                4.0,
                Particle.REDSTONE,
                new Particle.DustOptions(Color.YELLOW, 1.0f)
        );
    }
}