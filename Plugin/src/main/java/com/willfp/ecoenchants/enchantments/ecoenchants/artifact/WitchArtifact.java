package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class WitchArtifact extends Artifact {
    public WitchArtifact() {
        super(
                "witch_artifact",
                5.0,
                Particle.SPELL_WITCH
        );
    }
}