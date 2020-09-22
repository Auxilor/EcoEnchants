package com.willfp.ecoenchants.config.configs;

import com.willfp.ecoenchants.config.YamlConfig;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

/**
 * Wrapper for config.yml
 */
public class Target extends YamlConfig {
    public Target() {
        super("target");
    }

    public Set<String> getTargets() {
        return config.getConfigurationSection("targets").getKeys(false);
    }

    public Set<Material> getTargetMaterials(String target) {
        Set<Material> materials = new HashSet<>();
        config.getStringList("targets." + target).forEach((materialName) -> {
            materials.add(Material.getMaterial(materialName.toUpperCase()));
        });

        return materials;
    }
}
