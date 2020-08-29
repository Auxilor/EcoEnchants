package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

public class InkArtifact extends Artifact {
    public InkArtifact() {
        super(
                "ink_artifact",
                4.0,
                Particle.SQUID_INK
        );
    }
}