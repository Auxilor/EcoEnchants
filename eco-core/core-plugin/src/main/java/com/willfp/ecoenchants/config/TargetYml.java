package com.willfp.ecoenchants.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.yaml.YamlBaseConfig;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TargetYml extends YamlBaseConfig {
    /**
     * Instantiate target.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public TargetYml(@NotNull final EcoPlugin plugin) {
        super("target", false, plugin);
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

        materials.removeIf(Objects::isNull);

        return materials;
    }
}
