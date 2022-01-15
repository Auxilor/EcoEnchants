package com.willfp.ecoenchants.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.BaseConfig;
import com.willfp.eco.core.config.ConfigType;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TargetYml extends BaseConfig {
    /**
     * Instantiate target.yml.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public TargetYml(@NotNull final EcoPlugin plugin) {
        super("target", plugin, false, ConfigType.YAML);
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

    /**
     * Get the slot for a target name.
     *
     * @param target The target.
     * @return The slot, or {@link com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget.Slot#ANY}
     */
    public EnchantmentTarget.Slot getSlot(@NotNull final String target) {
        for (String str : this.getStrings("targets." + target)) {
            if (str.startsWith("slot:")) {
                return EnchantmentTarget.Slot.valueOf(
                        str.replace("slot:", "").toUpperCase()
                );
            }
        }

        return EnchantmentTarget.Slot.ANY;
    }
}
