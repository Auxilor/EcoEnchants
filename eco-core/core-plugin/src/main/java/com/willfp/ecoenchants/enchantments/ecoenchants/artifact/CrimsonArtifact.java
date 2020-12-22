package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;

public class CrimsonArtifact extends Artifact {
    public CrimsonArtifact() {
        super(
                "crimson_artifact",
                Prerequisite.MinVer1_16
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.CRIMSON_SPORE;
    }
}