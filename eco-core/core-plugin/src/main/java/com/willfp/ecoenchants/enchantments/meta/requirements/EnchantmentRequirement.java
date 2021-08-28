package com.willfp.ecoenchants.enchantments.meta.requirements;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class EnchantmentRequirement {
    /**
     * The ID of the requirement.
     */
    @Getter
    private final String id;

    protected EnchantmentRequirement(@NotNull final String id) {
        this.id = id;

        EnchantmentRequirements.addNewRequirement(this);
    }

    /**
     * Test if the player meets the requirement.
     *
     * @param player The player.
     * @param args   The arguments.
     * @return The requirement.
     */
    public abstract boolean doesPlayerMeet(@NotNull Player player,
                                           @NotNull List<String> args);
}
