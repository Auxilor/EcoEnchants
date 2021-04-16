package com.willfp.ecoenchants.mmo;

import com.willfp.eco.core.Prerequisite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class MMOPrerequisites {
    private static final Set<String> enabledPlugins = Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(Plugin::getName).collect(Collectors.toSet());

    public static final Prerequisite HAS_MMOCORE = new Prerequisite(
            () -> enabledPlugins.contains("MMOCore"),
            "Has mmocore installed"
    );
    public static final Prerequisite HAS_MMOITEMS = new Prerequisite(
            () -> enabledPlugins.contains("MMOItems"),
            "Has mmoitems installed"
    );

    public static Prerequisite[] append(Prerequisite[] array, Prerequisite newElement) {
        Prerequisite[] copy = new Prerequisite[array.length + 1];
        System.arraycopy(array, 0, copy, 0, array.length);
        copy[array.length] = newElement;
        return copy;
    }
}
