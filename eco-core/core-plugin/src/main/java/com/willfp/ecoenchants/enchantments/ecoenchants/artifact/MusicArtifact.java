package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class MusicArtifact extends Artifact {
    public MusicArtifact() {
        super(
                "music_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.NOTE;
    }
}
