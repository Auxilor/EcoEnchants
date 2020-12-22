package com.willfp.eco.util.drops.telekinesis;

import com.willfp.eco.util.lambda.ObjectBiCallable;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class TelekineticTests {
    private static final Set<ObjectBiCallable<Boolean, Player>> tests = new HashSet<>();

    public static void registerTest(ObjectBiCallable<Boolean, Player> test) {
        tests.add(test);
    }

    public static boolean testPlayer(Player player) {
        for (ObjectBiCallable<Boolean, Player> test : tests) {
            if(test.call(player)) return true;
        }

        return false;
    }
}
