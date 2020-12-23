package com.willfp.eco.util.drops.telekinesis;

import com.willfp.eco.util.lambda.ObjectInputCallable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface TelekinesisTests {
    /**
     * Register a new test to check against.
     *
     * @param test The test to register, where the boolean output is if the player is telekinetic.
     */
    void registerTest(@NotNull ObjectInputCallable<Boolean, Player> test);

    /**
     * Test the player for telekinesis.
     *
     * @param player The player to test.
     * @return If the player is telekinetic.
     */
    boolean testPlayer(@NotNull Player player);
}
