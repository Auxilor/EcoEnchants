package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
public class EnchantmentArtifact extends Artifact {
    public EnchantmentArtifact() {
        super(
                "enchantment_artifact"
        );
    }

    @Override
    protected Particle getParticle() {
        return Particle.ENCHANTMENT_TABLE;
    }
}