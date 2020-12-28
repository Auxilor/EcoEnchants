package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.BaseConfig;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper for config.yml
 */
public class Target extends BaseConfig {
    public Target() {
        super("target", false);
    }

    public Set<String> getTargets() {
        return this.getConfig().getConfigurationSection("targets").getKeys(false);
    }

    public Set<Material> getTargetMaterials(@NotNull final String target) {
        Set<Material> materials = new HashSet<>();
        this.getConfig().getStringList("targets." + target).forEach(materialName -> {
            materials.add(Material.getMaterial(materialName.toUpperCase()));
        });

        return materials;
    }
}
