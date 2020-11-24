package com.willfp.ecoenchants.util.optional;

import com.willfp.ecoenchants.util.ClassUtils;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class Prerequisite {
    public static final Prerequisite MinVer1_16 = new Prerequisite(
            false,
            "Requires minimum server version of 1.16"
    );
    public static final Prerequisite HasPaper = new Prerequisite(
            false,
            "Requires server to be running paper (or a fork)"
    );

    private boolean isMet;
    private final String description;

    protected Prerequisite(boolean isMet, String description) {
        this.isMet = isMet;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMet() {
        return isMet;
    }

    private void setMet(boolean met) {
        isMet = met;
    }

    static {
        update();
    }

    public static void update() {
        MinVer1_16.setMet(!Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("15"));
        HasPaper.setMet(ClassUtils.exists("com.destroystokyo.paper.event.player.PlayerElytraBoostEvent"));
    }

    public static boolean areMet(Prerequisite[] prerequisites) {
        return Arrays.stream(prerequisites).allMatch(Prerequisite::isMet);
    }
}
