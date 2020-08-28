package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;

@SuppressWarnings("deprecation")
public class DragonArtifact extends Artifact {
    public DragonArtifact() {
        super(
                "dragon_artifact",
                4.0,
                Particle.DRAGON_BREATH
        );
    }
}