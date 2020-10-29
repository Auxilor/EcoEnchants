package com.willfp.ecoenchants.integrations.anticheat;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

public class AnticheatManager {
    private static final Set<AnticheatWrapper> anticheats = new HashSet<>();

    public static boolean registerIfPresent(AnticheatWrapper anticheat) {
        if(Bukkit.getPluginManager().isPluginEnabled(anticheat.getPluginName())) {
            if(anticheat instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) anticheat, EcoEnchantsPlugin.getInstance());
            }

            anticheats.add(anticheat);
            return true;
        }
        return false;
    }

    public static void exemptPlayer(Player player) {
        anticheats.forEach(anticheat -> anticheat.exempt(player));
    }

    public static void unexemptPlayer(Player player) {
        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            anticheats.forEach(anticheat -> anticheat.unexempt(player));
        }, 1);
    }
}
