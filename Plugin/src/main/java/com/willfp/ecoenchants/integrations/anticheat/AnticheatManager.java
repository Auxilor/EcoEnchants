package com.willfp.ecoenchants.integrations.anticheat;

import com.willfp.ecoenchants.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class AnticheatManager {
    private static final Set<AnticheatWrapper> anticheats = new HashSet<>();

    public static void registerAnticheat(AnticheatWrapper anticheat) {
        if(anticheat instanceof Listener) {
            Bukkit.getPluginManager().registerEvents((Listener) anticheat, Main.getInstance());
        }

        anticheats.add(anticheat);
    }

    public static void exemptPlayer(Player player) {
        anticheats.forEach(anticheat -> anticheat.exempt(player));
    }

    public static void unexemptPlayer(Player player) {
        anticheats.forEach(anticheat -> anticheat.unexempt(player));
    }
}
