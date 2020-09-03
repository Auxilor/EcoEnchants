package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class LavaArtifact extends Artifact {
    public LavaArtifact() {
        super(
                "lava_artifact",
                5.0,
                Particle.DRIP_LAVA
        );
    }
}