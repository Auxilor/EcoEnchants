package com.willfp.eco.util.bukkit.scheduling;

import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class EcoBukkitRunnable extends BukkitRunnable {
    protected final AbstractEcoPlugin plugin;

    protected EcoBukkitRunnable(AbstractEcoPlugin plugin) {
        this.plugin = plugin;
    }
}
