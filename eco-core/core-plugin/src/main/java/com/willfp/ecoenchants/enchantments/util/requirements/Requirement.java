package com.willfp.ecoenchants.enchantments.util.requirements;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Requirement {
    /**
     * If the requirement is met for a player.
     *
     * @param player The player.
     * @param args   The args.
     * @return If met.
     */
    boolean isMetBy(@NotNull Player player,
                    @NotNull List<String> args);
}
