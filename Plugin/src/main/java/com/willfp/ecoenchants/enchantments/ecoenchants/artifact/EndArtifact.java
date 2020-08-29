package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

public class EndArtifact extends Artifact {
    public EndArtifact() {
        super(
                "end_artifact",
                4.0,
                Particle.END_ROD
        );
    }
}