package com.willfp.eco.util.drops.telekinesis;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.lambda.ObjectBiCallable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class TelekineticTests extends PluginDependent {
    private final Set<ObjectBiCallable<Boolean, Player>> tests = new HashSet<>();

    public TelekineticTests(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    public void registerTest(ObjectBiCallable<Boolean, Player> test) {
        tests.add(test);
    }

    public boolean testPlayer(Player player) {
        for (ObjectBiCallable<Boolean, Player> test : tests) {
            if(test.call(player)) return true;
        }

        return false;
    }
}
