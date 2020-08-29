package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

public class MagicArtifact extends Artifact {
    public MagicArtifact() {
        super(
                "magic_artifact",
                4.0,
                Particle.CRIT_MAGIC
        );
    }
}