package com.willfp.eco.util.proxy;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class ProxyConstants {
    /**
     * The NMS version that the server is running on.
     */
    public static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
}
