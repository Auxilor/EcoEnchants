package com.willfp.eco.util.drops.telekinesis;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface TelekinesisTests {
    /**
     * Register a new test to check against.
     *
     * @param test The test to register, where the boolean output is if the player is telekinetic.
     */
    void registerTest(@NotNull Function<Player, Boolean> test);

    /**
     * Test the player for telekinesis.
     *
     * @param player The player to test.
     * @return If the player is telekinetic.
     */
    boolean testPlayer(@NotNull Player player);
}
