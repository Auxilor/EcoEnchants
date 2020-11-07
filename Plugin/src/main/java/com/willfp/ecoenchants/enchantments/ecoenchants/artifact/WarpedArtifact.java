package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Particle;

public final class WarpedArtifact extends Artifact {
    public WarpedArtifact() {
        super(
                "warped_artifact",
                Prerequisite.MinVer1_16
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.WARPED_SPORE;
    }
}