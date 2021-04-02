package com.willfp.ecoenchants.config.configs;

import com.willfp.eco.util.config.BaseConfig;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Target extends BaseConfig {
    /**
     * Instantiate target.yml.
     */
    public Target() {
        super("target", false, EcoEnchantsPlugin.getInstance());
    }

    /**
     * Get all target names.
     *
     * @return Set of all names.
     */
    public List<String> getTargets() {
        return this.getSubsection("targets").getKeys(false);
    }

    /**
     * Get all materials from a target name.
     *
     * @param target The name of the target.
     * @return All materials.
     */
    public Set<Material> getTargetMaterials(@NotNull final String target) {
        Set<Material> materials = new HashSet<>();
        this.getStrings("targets." + target).forEach(materialName -> {
            materials.add(Material.getMaterial(materialName.toUpperCase()));
        });

        return materials;
    }
}
