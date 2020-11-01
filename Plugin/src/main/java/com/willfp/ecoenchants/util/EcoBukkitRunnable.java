package com.willfp.ecoenchants.util;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public abstract class EcoBukkitRunnable extends BukkitRunnable {
    private static final Set<Integer> tasks = new HashSet<>();

    private final int ecoID;

    public EcoBukkitRunnable(int ecoID) {
        this.ecoID = ecoID;
    }

    public int getEcoID() {
        return ecoID;
    }

    @Override
    public final void run() {
        if(tasks.contains(this.getEcoID())) {
            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
                tasks.remove(this.getEcoID());
            }, 1);
            return;
        }

        onRun();
        tasks.add(this.getEcoID());
    }

    public abstract void onRun();
}
