package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public final class DragonArtifact extends Artifact {
    public DragonArtifact() {
        super(
                "dragon_artifact",
                5.0,
                Particle.DRAGON_BREATH
        );
    }
}