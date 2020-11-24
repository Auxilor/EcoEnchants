package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class LavaArtifact extends Artifact {
    public LavaArtifact() {
        super(
                "lava_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.DRIP_LAVA;
    }
}