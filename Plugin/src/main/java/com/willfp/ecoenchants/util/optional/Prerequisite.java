package com.willfp.ecoenchants.util.optional;

import com.willfp.ecoenchants.util.ClassUtils;
import com.willfp.ecoenchants.util.interfaces.ObjectCallable;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An object representing a condition that must be met in order to perform an action
 */
public class Prerequisite {
    private static final List<Prerequisite> values = new ArrayList<>();

    /**
     * Requires the server to be running minecraft version 1.16 or higher
     */
    public static final Prerequisite MinVer1_16 = new Prerequisite(
            () -> !Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("15"),
            "Requires minimum server version of 1.16"
    );

    /**
     * Requires the server to be running an implementation of paper
     */
    public static final Prerequisite HasPaper = new Prerequisite(
            () -> ClassUtils.exists("com.destroystokyo.paper.event.player.PlayerElytraBoostEvent"),
            "Requires server to be running paper (or a fork)"
    );

    private boolean isMet;
    private final ObjectCallable<Boolean> isMetCallable;
    private final String description;

    /**
     * Create a prerequisite
     *
     * @param isMetCallable An {@link ObjectCallable<Boolean>} that returns if the prerequisite is met
     * @param description   The description of the prerequisite, shown to the user if it isn't
     */
    public Prerequisite(ObjectCallable<Boolean> isMetCallable, String description) {
        this.isMetCallable = isMetCallable;
        this.isMet = isMetCallable.call();
        this.description = description;
        values.add(this);
    }

    /**
     * Get the description of the prerequisite
     *
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get if the prerequisite has been met
     *
     * @return If the prerequisite is met
     */
    public boolean isMet() {
        return isMet;
    }

    private void refresh() {
        this.isMet = this.isMetCallable.call();
    }

    /**
     * Update all prerequisites' {@link Prerequisite#isMet}
     */
    public static void update() {
        values.forEach(Prerequisite::refresh);
    }

    /**
     * Check if all prerequisites in array are met
     *
     * @param prerequisites A primitive array of prerequisites to check
     * @return If all the prerequisites are met
     */
    public static boolean areMet(Prerequisite[] prerequisites) {
        update();
        return Arrays.stream(prerequisites).allMatch(Prerequisite::isMet);
    }
}
