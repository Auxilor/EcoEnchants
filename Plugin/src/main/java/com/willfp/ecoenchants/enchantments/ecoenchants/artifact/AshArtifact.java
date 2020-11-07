package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Particle;

public final class AshArtifact extends Artifact {
    public AshArtifact() {
        super(
                "ash_artifact",
                Prerequisite.MinVer1_16
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.WHITE_ASH;
    }
}