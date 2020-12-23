package com.willfp.eco.util.drops.telekinesis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class TelekinesisUtils {
    /**
     * The test service registered to bukkit.
     */
    private static TelekinesisTests tests = Bukkit.getServicesManager().load(TelekinesisTests.class);

    /**
     * Test the player for telekinesis.
     * <p>
     * If any test returns true, so does this.
     *
     * @param player The player to test.
     * @return If the player is telekinetic.
     */
    public static boolean testPlayer(@NotNull final Player player) {
        return tests.testPlayer(player);
    }

    /**
     * Update the test to use.
     */
    public static void update() {
        tests = Bukkit.getServicesManager().load(TelekinesisTests.class);
    }

    private TelekinesisUtils() {

    }
}
