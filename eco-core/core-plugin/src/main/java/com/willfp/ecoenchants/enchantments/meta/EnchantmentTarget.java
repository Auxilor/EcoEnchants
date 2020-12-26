package com.willfp.ecoenchants.enchantments.meta;

import com.google.common.collect.ImmutableSet;
import com.willfp.eco.util.config.annotations.ConfigUpdater;
import com.willfp.eco.util.interfaces.Registerable;
import com.willfp.eco.util.interfaces.Updatable;
import com.willfp.ecoenchants.config.EcoEnchantsConfigs;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Class for storing all enchantment rarities
 */
public class EnchantmentTarget implements Registerable, Updatable {
    private static final Set<EnchantmentTarget> REGISTERED = new HashSet<>();
    public static final EnchantmentTarget ALL = new EnchantmentTarget("all", new HashSet<>());

    static {
        REGISTERED.add(ALL);
    }

    private final String name;
    private final Set<Material> materials;

    /**
     * Create new EnchantmentRarity
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

    @Override
    public void register() {
        Optional<EnchantmentTarget> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(REGISTERED::remove);
        matching.ifPresent(enchantmentTarget -> ALL.materials.removeAll(enchantmentTarget.getMaterials()));
        REGISTERED.add(this);
        ALL.materials.addAll(this.getMaterials());
    }

    /**
     * Get the name of the rarity
     *
     * @return The name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the materials of the rarity
     *
     * @return The materials
     */
    public Set<Material> getMaterials() {
        return ImmutableSet.copyOf(this.materials);
    }

    /**
     * Get EnchantmentTarget matching name
     *
     * @param name The name to search for
     *
     * @return The matching EnchantmentTarget, or null if not found
     */
    public static EnchantmentTarget getByName(@NotNull final String name) {
        Optional<EnchantmentTarget> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Update all targets
     * Called on /ecoreload
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
     * Get all rarities
     *
     * @return A set of all rarities
     */
    public static Set<EnchantmentTarget> values() {
        return REGISTERED;
    }

    static {
        update();
    }
}
