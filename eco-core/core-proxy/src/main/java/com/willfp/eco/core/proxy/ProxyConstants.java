package com.willfp.eco.core.proxy;

import org.bukkit.Bukkit;

public class ProxyConstants {
    /**
     * The NMS version that the server is running on.
     */
    public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
}
