package com.willfp.eco.util.drops.telekinesis;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.lambda.ObjectBiCallable;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class EcoTelekinesisTests extends PluginDependent implements TelekinesisTests {
    private final Set<ObjectBiCallable<Boolean, Player>> tests = new HashSet<>();

    public EcoTelekinesisTests(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void registerTest(ObjectBiCallable<Boolean, Player> test) {
        tests.add(test);
    }

    @Override
    public boolean testPlayer(Player player) {
        for (ObjectBiCallable<Boolean, Player> test : tests) {
            if(test.call(player)) return true;
        }

        return false;
    }
}
