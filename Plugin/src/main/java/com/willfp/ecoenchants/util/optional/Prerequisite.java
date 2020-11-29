package com.willfp.ecoenchants.util.optional;

import com.willfp.ecoenchants.util.ClassUtils;
import com.willfp.ecoenchants.util.interfaces.ObjectCallable;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Prerequisite {
    private static final List<Prerequisite> values = new ArrayList<>();

    public static final Prerequisite MinVer1_16 = new Prerequisite(
            () -> !Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("15"),
            "Requires minimum server version of 1.16"
    );
    public static final Prerequisite HasPaper = new Prerequisite(
            () -> ClassUtils.exists("com.destroystokyo.paper.event.player.PlayerElytraBoostEvent"),
            "Requires server to be running paper (or a fork)"
    );

    private boolean isMet;
    private final ObjectCallable<Boolean> isMetCallable;
    private final String description;

    public Prerequisite(ObjectCallable<Boolean> isMetCallable, String description) {
        this.isMetCallable = isMetCallable;
        this.isMet = isMetCallable.call();
        this.description = description;
        values.add(this);
    }

    public String getDescription() {
        return description;
    }

    public boolean isMet() {
        return isMet;
    }

    private void refresh() {
        this.isMet = this.isMetCallable.call();
    }

    public static void update() {
        values.forEach(Prerequisite::refresh);
    }

    public static boolean areMet(Prerequisite[] prerequisites) {
        update();
        return Arrays.stream(prerequisites).allMatch(Prerequisite::isMet);
    }
}
