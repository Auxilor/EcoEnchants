package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Particle;

public final class SoulFireArtifact extends Artifact {
    public SoulFireArtifact() {
        super(
                "soul_fire_artifact",
                Prerequisite.MinVer1_16
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.SOUL_FIRE_FLAME;
    }
}