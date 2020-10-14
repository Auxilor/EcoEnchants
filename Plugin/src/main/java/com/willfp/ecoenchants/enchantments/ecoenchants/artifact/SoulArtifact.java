package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Particle;

public final class SoulArtifact extends Artifact {
    public SoulArtifact() {
        super(
                "soul_artifact",
                5.0,
                Particle.SOUL,
                new Prerequisite[]{Prerequisite.MinVer1_16}
        );
    }
}