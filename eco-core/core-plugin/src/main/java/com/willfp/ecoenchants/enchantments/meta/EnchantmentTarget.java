package com.willfp.ecoenchants.enchantments.meta;

import com.google.common.collect.ImmutableSet;
import com.willfp.eco.core.config.updating.ConfigUpdater;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class EnchantmentTarget {
    /**
     * Target containing the materials from all other targets.
     */
    public static final EnchantmentTarget ALL = new EnchantmentTarget("all", new HashSet<>(), Slot.ANY);
    /**
     * All registered targets.
     */
    private static final Set<EnchantmentTarget> REGISTERED = new HashSet<>();

    static {
        REGISTERED.add(ALL);
        update(EcoEnchantsPlugin.getInstance());
    }

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
     * The slot to check for custom enchants.
     */
    @Getter
    private final Slot slot;

    /**
     * Create new rarity.
     *
     * @param name      The name of the rarity
     * @param materials The items for the target
     */
    public EnchantmentTarget(@NotNull final String name,
                             @NotNull final Set<Material> materials,
                             @NotNull final Slot slot) {
        this.name = name;
        materials.removeIf(Objects::isNull);
        this.materials = materials;
        this.slot = slot;
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
     *
     * @param plugin Instance of EcoEnchants.
     */
    @ConfigUpdater
    public static void update(@NotNull final EcoEnchantsPlugin plugin) {
        List<String> targetNames = plugin.getTargetYml().getTargets();
        ALL.materials.clear();
        targetNames.forEach(name -> {
            Set<Material> materials = plugin.getTargetYml().getTargetMaterials(name);
            new EnchantmentTarget(name, materials, plugin.getTargetYml().getSlot(name)).register();
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

    private void register() {
        Optional<EnchantmentTarget> matching = REGISTERED.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst();
        matching.ifPresent(REGISTERED::remove);
        matching.ifPresent(enchantmentTarget -> ALL.getMaterials().removeAll(enchantmentTarget.getMaterials()));
        REGISTERED.add(this);
        ALL.getMaterials().addAll(this.getMaterials());
    }

    /**
     * Enchant slots.
     */
    public enum Slot {
        /**
         * In hands.
         */
        HANDS,

        /**
         * In armor.
         */
        ARMOR,

        /**
         * In inventory.
         */
        ANY
    }
}
