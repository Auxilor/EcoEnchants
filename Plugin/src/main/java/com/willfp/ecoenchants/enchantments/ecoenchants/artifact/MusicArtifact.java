package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class MusicArtifact extends Artifact {
    public MusicArtifact() {
        super(
                "music_artifact",
                4.0,
                Particle.NOTE
        );
    }
}