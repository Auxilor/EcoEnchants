package com.willfp.eco.util.drops.telekinesis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TelekinesisUtils {
    private static TelekinesisTests tests = Bukkit.getServicesManager().load(TelekinesisTests.class);

    public static boolean testPlayer(Player player) {
        return tests.testPlayer(player);
    }

    public static void update() {
        tests = Bukkit.getServicesManager().load(TelekinesisTests.class);
    }
}
