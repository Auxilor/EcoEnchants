package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.Artifact;
import org.bukkit.Particle;
public class EnchantmentArtifact extends Artifact {
    public EnchantmentArtifact() {
        super(
                "enchantment_artifact",
                5.0,
                Particle.ENCHANTMENT_TABLE
        );
    }
}