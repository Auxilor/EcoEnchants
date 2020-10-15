package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class TotemArtifact extends Artifact {
    public TotemArtifact() {
        super(
                "totem_artifact",
                5.0
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.TOTEM;
    }
}