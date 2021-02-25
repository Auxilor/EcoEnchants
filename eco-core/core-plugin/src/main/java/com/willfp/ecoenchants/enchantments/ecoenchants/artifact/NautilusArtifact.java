package com.willfp.ecoenchants.enchantments.ecoenchants.artifact;

import com.willfp.ecoenchants.enchantments.itemtypes.Artifact;
import org.bukkit.Particle;
import org.jetbrains.annotations.NotNull;

public class NautilusArtifact extends Artifact {
    public NautilusArtifact() {
        super(
                "nautilus_artifact"
        );
    }

    @Override
    public @NotNull Particle getParticle() {
        return Particle.NAUTILUS;
    }
}
