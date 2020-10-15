package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Particle;

public final class CrimsonArtifact extends Artifact {
    public CrimsonArtifact() {
        super(
                "crimson_artifact",
                new Prerequisite[]{Prerequisite.MinVer1_16}
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.CRIMSON_SPORE;
    }
}