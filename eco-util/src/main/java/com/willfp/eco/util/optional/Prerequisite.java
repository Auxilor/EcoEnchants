package com.willfp.eco.util.optional;

import com.willfp.eco.util.ClassUtils;
import com.willfp.eco.util.lambda.ObjectCallable;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Prerequisite {
    /**
     * All existing prerequisites are registered on creation.
     */
    private static final List<Prerequisite> VALUES = new ArrayList<>();

    /**
     * Requires the server to be running minecraft version 1.16 or higher.
     */
    public static final Prerequisite MINIMUM_1_16 = new Prerequisite(
            () -> !Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("15"),
            "Requires minimum server version of 1.16"
    );

    /**
     * Requires the server to be running an implementation of paper.
     */
    public static final Prerequisite HAS_PAPER = new Prerequisite(
            () -> ClassUtils.exists("com.destroystokyo.paper.event.player.PlayerElytraBoostEvent"),
            "Requires server to be running paper (or a fork)"
    );

    /**
     * If the necessary prerequisite condition has been met.
     */
    @Getter
    private boolean isMet;

    /**
     * Retrieve if the necessary prerequisite condition is met.
     */
    private final ObjectCallable<Boolean> isMetCallable;

    /**
     * The description of the requirements of the prerequisite.
     */
    @Getter
    private final String description;

    /**
     * Create a prerequisite.
     *
     * @param isMetCallable An {@link ObjectCallable<Boolean>} that returns if the prerequisite is met
     * @param description   The description of the prerequisite, shown to the user if it isn't
     */
    public Prerequisite(@NotNull final ObjectCallable<Boolean> isMetCallable,
                        @NotNull final String description) {
        this.isMetCallable = isMetCallable;
        this.isMet = isMetCallable.call();
        this.description = description;
        VALUES.add(this);
    }

    /**
     * Refresh the condition set in the callable, updates {@link this#isMet}.
     */
    private void refresh() {
        this.isMet = this.isMetCallable.call();
    }

    /**
     * Update all prerequisites' {@link Prerequisite#isMet}.
     */
    public static void update() {
        VALUES.forEach(Prerequisite::refresh);
    }

    /**
     * Check if all prerequisites in array are met.
     *
     * @param prerequisites A primitive array of prerequisites to check.
     * @return If all the prerequisites are met.
     */
    public static boolean areMet(@NotNull final Prerequisite[] prerequisites) {
        update();
        return Arrays.stream(prerequisites).allMatch(Prerequisite::isMet);
    }

    static {
        update();
    }
}
