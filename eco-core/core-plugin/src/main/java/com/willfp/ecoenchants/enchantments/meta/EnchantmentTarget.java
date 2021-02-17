package com.willfp.ecoenchants.enchantments.meta;

import com.google.common.collect.ImmutableSet;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.ecoenchants.config.EcoEnchantsConfigs;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class EnchantmentTarget {
    /**
     * All registered targets.
     */
    private static final Set<EnchantmentTarget> REGISTERED = new HashSet<>();

    /**
     * Target containing the materials from all other targets.
     */
    public static final EnchantmentTarget ALL = new EnchantmentTarget("all", new HashSet<>());

    /**
     * The name of the target.
     */
    @Getter
    private final String name;

    /**
     * The materials of the target.
     */
    @Getter
    private final Set<Material> materials;

    /**
     * Create new rarity.
     *
     * @param name      The name of the rarity
     * @param materials The items for the target
     */
    public EnchantmentTarget(@NotNull final String name,
                             @NotNull final Set<Material> materials) {
        this.name = name;
        materials.removeIf(Objects::isNull);
        this.materials = materials;
    }

    public void register() {
        Optional<EnchantmentTarget> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(REGISTERED::remove);
        matching.ifPresent(enchantmentTarget -> ALL.getMaterials().removeAll(enchantmentTarget.getMaterials()));
        REGISTERED.add(this);
        ALL.getMaterials().addAll(this.getMaterials());
    }

    /**
     * Get EnchantmentTarget matching name.
     *
     * @param name The name to search for.
     * @return The matching EnchantmentTarget, or null if not found.
     */
    public static EnchantmentTarget getByName(@NotNull final String name) {
        Optional<EnchantmentTarget> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Update all targets.
     */
    @ConfigUpdater
    public static void update() {
        Set<String> targetNames = EcoEnchantsConfigs.TARGET.getTargets();
        ALL.materials.clear();
        targetNames.forEach(name -> {
            Set<Material> materials = EcoEnchantsConfigs.TARGET.getTargetMaterials(name);
            new EnchantmentTarget(name, materials).register();
        });
    }

    /**
     * Get all rarities.
     *
     * @return A set of all rarities.
     */
    public static Set<EnchantmentTarget> values() {
        return ImmutableSet.copyOf(REGISTERED);
    }

    static {
        REGISTERED.add(ALL);
        update();
    }
}
