package com.willfp.ecoenchants.util.optional;

import com.willfp.ecoenchants.util.ClassUtils;
import org.bukkit.Bukkit;

import java.util.Arrays;

public enum Prerequisite {
    MinVer1_16(
            false,
            "Requires minimum server version of 1.16"
    ),
    HasPaper(
            false,
            "Requires server to be running paper (or a fork)"
    );

    private boolean isMet;
    private final String description;

    Prerequisite(boolean isMet, String description) {
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
        MinVer1_16.setMet(!Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("15"));
        HasPaper.setMet(ClassUtils.exists("com.destroystokyo.paper.event.player.PlayerElytraBoostEvent"));
    }

    public static boolean areMet(Prerequisite[] prerequisites) {
        return Arrays.stream(prerequisites).allMatch(Prerequisite::isMet);
    }
}
