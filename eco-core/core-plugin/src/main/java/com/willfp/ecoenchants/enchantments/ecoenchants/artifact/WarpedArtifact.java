package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;

public class WarpedArtifact extends Artifact {
    public WarpedArtifact() {
        super(
                "warped_artifact",
                Prerequisite.MINIMUM_1_16
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.WARPED_SPORE;
    }
}
