package com.willfp.eco.util.drops.telekinesis;

import com.willfp.eco.util.lambda.ObjectBiCallable;
import org.bukkit.entity.Player;

public interface TelekinesisTests {
    void registerTest(ObjectBiCallable<Boolean, Player> test);
    boolean testPlayer(Player player);
}
