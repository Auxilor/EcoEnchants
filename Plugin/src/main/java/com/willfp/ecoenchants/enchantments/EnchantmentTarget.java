package com.willfp.ecoenchants.enchantments;

import com.willfp.ecoenchants.config.ConfigManager;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Class for storing all enchantment rarities
 */
public class EnchantmentTarget {
    private static final Set<EnchantmentTarget> targets = new HashSet<>();
    public static final Set<Material> ALL = new HashSet<>();

    private final String name;
    private final Set<Material> materials;

    /**
     * Create new EnchantmentRarity
     * @param name The name of the rarity
     * @paran materials The items for the target
     */
    public EnchantmentTarget(String name, Set<Material> materials) {
        this(name, materials, false);
    }

    /**
     * Create new EnchantmentRarity
     * @param name The name of the rarity
     * @paran materials The items for the target
     * @param noRegister Dont register internally
     */
    public EnchantmentTarget(String name, Set<Material> materials, boolean noRegister) {
        Optional<EnchantmentTarget> matching = targets.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(targets::remove);
        matching.ifPresent(enchantmentTarget -> ALL.removeAll(enchantmentTarget.getMaterials()));

        this.name = name;
        this.materials = materials;

        if(!noRegister) {
            targets.add(this);
            ALL.addAll(materials);
        }
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
        return this.materials;
    }

    /**
     * Get EnchantmentTarget matching name
     * @param name The name to search for
     * @return The matching EnchantmentTarget, or null if not found
     */
    public static EnchantmentTarget getByName(String name) {
        if(name.equalsIgnoreCase("all")) return new EnchantmentTarget("all", EnchantmentTarget.ALL, true);

        Optional<EnchantmentTarget> matching = targets.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        return matching.orElse(null);
    }

    /**
     * Update all targets
     * Called on /ecoreload
     */
    public static void update() {
        Set<String> targetNames = ConfigManager.getTarget().getTargets();
        targetNames.forEach((target) -> {
            String name = target;
            Set<Material> materials = ConfigManager.getTarget().getTargetMaterials(target);

            new EnchantmentTarget(name, materials);
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
