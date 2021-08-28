package com.willfp.ecoenchants.enchantments.meta.requirements;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.requirements.requirements.RequirementHasPermission;
import com.willfp.ecoenchants.enchantments.meta.requirements.requirements.RequirementPlaceholderEquals;
import com.willfp.ecoenchants.enchantments.meta.requirements.requirements.RequirementPlaceholderGreaterThan;
import com.willfp.ecoenchants.enchantments.meta.requirements.requirements.RequirementPlaceholderLessThan;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
@SuppressWarnings({"unused", "checkstyle:JavadocVariable"})
public class EnchantmentRequirements {
    /**
     * All registered requirements.
     */
    private static final BiMap<String, EnchantmentRequirement> BY_ID = HashBiMap.create();

    public static final EnchantmentRequirement HAS_PERMISSION = new RequirementHasPermission();
    public static final EnchantmentRequirement PLACEHOLDER_EQUALS = new RequirementPlaceholderEquals();
    public static final EnchantmentRequirement PLACEHOLDER_GREATER_THAN = new RequirementPlaceholderGreaterThan();
    public static final EnchantmentRequirement PLACEHOLDER_LESS_THAN = new RequirementPlaceholderLessThan();

    /**
     * Get all registered {@link EcoEnchant}s.
     *
     * @return A list of all {@link EcoEnchant}s.
     */
    public static List<EnchantmentRequirement> values() {
        return ImmutableList.copyOf(BY_ID.values());
    }

    /**
     * Get {@link EnchantmentRequirement} matching ID.
     *
     * @param name The ID to search for.
     * @return The matching {@link EnchantmentRequirement}, or null if not found.
     */
    public static EnchantmentRequirement getByID(@NotNull final String name) {
        return BY_ID.get(name);
    }

    /**
     * Add new {@link EnchantmentRequirement} to EcoEnchants.
     * <p>
     * Only for internal use, requirements are automatically added in the constructor.
     *
     * @param req The {@link EnchantmentRequirement} to add.
     */
    public static void addNewRequirement(@NotNull final EnchantmentRequirement req) {
        BY_ID.inverse().remove(req);
        BY_ID.put(req.getId(), req);
    }

    /**
     * Remove {@link EnchantmentRequirement} from EcoEnchants.
     *
     * @param req The {@link EnchantmentRequirement} to remove.
     */
    public static void removeRequirement(@NotNull final EnchantmentRequirement req) {
        BY_ID.inverse().remove(req);
    }
}
