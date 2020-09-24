package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class MusicArtifact extends Artifact {
    public MusicArtifact() {
        super(
                "music_artifact",
                5.0,
                Particle.NOTE
        );
    }
}