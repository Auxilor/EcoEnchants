package com.willfp.eco.util.drops.telekinesis;

import com.willfp.eco.util.lambda.ObjectInputCallable;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class EcoTelekinesisTests implements TelekinesisTests {
    /**
     * Set of tests that return if the player is telekinetic.
     */
    private final Set<ObjectInputCallable<Boolean, Player>> tests = new HashSet<>();

    /**
     * Register a new test to check against.
     *
     * @param test The test to register, where the boolean output is if the player is telekinetic.
     */
    @Override
    public void registerTest(@NotNull final ObjectInputCallable<Boolean, Player> test) {
        tests.add(test);
    }

    /**
     * Test the player for telekinesis.
     * <p>
     * If any test returns true, so does this.
     *
     * @param player The player to test.
     * @return If the player is telekinetic.
     */
    @Override
    public boolean testPlayer(@NotNull final Player player) {
        for (ObjectInputCallable<Boolean, Player> test : tests) {
            if (test.call(player)) {
                return true;
            }
        }

        return false;
    }
}
