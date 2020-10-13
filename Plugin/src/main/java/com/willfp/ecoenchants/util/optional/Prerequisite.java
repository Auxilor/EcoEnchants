package com.willfp.ecoenchants.util.optional;

import com.willfp.ecoenchants.util.ClassUtils;
import org.bukkit.Bukkit;

public enum Prerequisite {
    MinVer1_16(false),
    HasPaper(false);

    private boolean isMet;

    Prerequisite(boolean isMet) {
        this.isMet = isMet;
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
}
