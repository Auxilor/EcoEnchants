package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.API.NMSEnchantManagerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

public class NMSEnchantManager {
    private static NMSEnchantManagerWrapper nmsEnchantManagerWrapper;

    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + version + ".NMSEnchantManager");
            if (NMSEnchantManagerWrapper.class.isAssignableFrom(class2)) {
                nmsEnchantManagerWrapper = (NMSEnchantManagerWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            nmsEnchantManagerWrapper = null;
        }
        return nmsEnchantManagerWrapper != null;
    }

    public static void registerNMS(Enchantment enchantment) {
        nmsEnchantManagerWrapper.init(enchantment);
    }

    public static void printDebug() {
        nmsEnchantManagerWrapper.debug();
    }
}
