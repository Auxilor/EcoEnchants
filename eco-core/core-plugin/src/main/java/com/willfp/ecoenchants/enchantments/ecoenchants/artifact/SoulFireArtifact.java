package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;

public class SoulFireArtifact extends Artifact {
    public SoulFireArtifact() {
        super(
                "soul_fire_artifact",
                Prerequisite.MinVer1_16
        );
    }

    @Override
    public Particle getParticle() {
        return Particle.SOUL_FIRE_FLAME;
    }
}