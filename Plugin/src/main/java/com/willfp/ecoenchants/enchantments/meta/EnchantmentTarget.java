package com.willfp.ecoenchants.enchantments.meta;

import com.google.common.collect.ImmutableSet;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.util.Registerable;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Class for storing all enchantment rarities
 */
public class EnchantmentTarget implements Registerable {
    private static final Set<EnchantmentTarget> targets = new HashSet<>();
    public static final EnchantmentTarget ALL = new EnchantmentTarget("all", new HashSet<>());

    static {
        targets.add(ALL);
    }

    private final String name;
    private final Set<Material> materials;

    /**
     * Create new EnchantmentRarity
     * @param name The name of the rarity
     * @param materials The items for the target
     */
    public EnchantmentTarget(String name, Set<Material> materials) {
        this.name = name;
        this.materials = materials;
    }

    @Override
    public void register() {
        Optional<EnchantmentTarget> matching = targets.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(targets::remove);
        matching.ifPresent(enchantmentTarget -> ALL.materials.removeAll(enchantmentTarget.getMaterials()));
        targets.add(this);
        ALL.materials.addAll(this.getMaterials());
    }

    /**
     * Get the name of the rarity
     * @return The name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the materials of the rarity
     * @return The materials
     */
    public Set<Material> getMaterials() {
        return ImmutableSet.copyOf(this.materials);
    }

    /**
     * Get EnchantmentTarget matching name
     * @param name The name to search for
     * @return The matching EnchantmentTarget, or null if not found
     */
    public static EnchantmentTarget getByName(String name) {
        Optional<EnchantmentTarget> matching = targets.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Update all targets
     * Called on /ecoreload
     */
    public static void update() {
        Set<String> targetNames = ConfigManager.getTarget().getTargets();
        ALL.materials.clear();
        targetNames.forEach((name) -> {
            Set<Material> materials = ConfigManager.getTarget().getTargetMaterials(name);
            new EnchantmentTarget(name, materials).register();
        });
    }

    /**
     * Get all rarities
     * @return A set of all rarities
     */
    public static Set<EnchantmentTarget> getAll() {
        return targets;
    }
}
