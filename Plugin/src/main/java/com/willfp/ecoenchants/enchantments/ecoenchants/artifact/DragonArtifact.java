package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class DragonArtifact extends Artifact {
    public DragonArtifact() {
        super(
                "dragon_artifact",
                5.0,
                Particle.DRAGON_BREATH
        );
    }
}