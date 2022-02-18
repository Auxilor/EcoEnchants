package com.willfp.ecoenchants.enchantments.util.requirements;

import org.jetbrains.annotations.NotNull;

public final class Requirements {
    /**
     * Requires a player to have a permission.
     */
    public static final Requirement HAS_PERMISSION = new RequirementHasPermission();

    /**
     * Placeholder equals value.
     */
    public static final Requirement PLACEHOLDER_EQUALS = new RequirementPlaceholderEquals();

    /**
     * Numeric placeholder greater than value.
     */
    public static final Requirement PLACEHOLDER_GREATER_THAN = new RequirementPlaceholderGreaterThan();

    /**
     * Numeric placeholder less than value.
     */
    public static final Requirement PLACEHOLDER_LESS_THAN = new RequirementPlaceholderLessThan();

    /**
     * Get Requirements matching ID.
     *
     * @param name The ID to search for.
     * @return The matching Requirements.
     */
    @NotNull
    public static Requirement getByID(@NotNull final String name) {
        return switch (name.toLowerCase()) {
            case "has-permission" -> HAS_PERMISSION;
            case "placeholder-equals" -> PLACEHOLDER_EQUALS;
            case "placeholder-greater-than" -> PLACEHOLDER_GREATER_THAN;
            case "placeholder-less-than" -> PLACEHOLDER_LESS_THAN;
            default -> new RequirementTrue();
        };
    }

    private Requirements() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}