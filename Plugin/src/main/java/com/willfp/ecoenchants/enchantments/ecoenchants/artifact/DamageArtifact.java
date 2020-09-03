package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class DamageArtifact extends Artifact {
    public DamageArtifact() {
        super(
                "damage_artifact",
                5.0,
                Particle.DAMAGE_INDICATOR
        );
    }
}