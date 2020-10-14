package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Particle;

public final class CrimsonArtifact extends Artifact {
    public CrimsonArtifact() {
        super(
                "crimson_artifact",
                5.0,
                Particle.CRIMSON_SPORE,
                new Prerequisite[]{Prerequisite.MinVer1_16}
        );
    }
}