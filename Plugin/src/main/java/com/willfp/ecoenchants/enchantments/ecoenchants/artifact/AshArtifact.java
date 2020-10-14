package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Particle;

public final class AshArtifact extends Artifact {
    public AshArtifact() {
        super(
                "ash_artifact",
                5.0,
                Particle.WHITE_ASH,
                new Prerequisite[]{Prerequisite.MinVer1_16}
        );
    }
}