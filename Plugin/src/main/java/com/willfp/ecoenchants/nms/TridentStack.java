package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.API.TridentStackWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

public class TridentStack {
    private static TridentStackWrapper tridentStackWrapper;

    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + version + ".TridentStack");
            if (TridentStackWrapper.class.isAssignableFrom(class2)) {
                tridentStackWrapper = (TridentStackWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            tridentStackWrapper = null;
        }
        return tridentStackWrapper != null;
    }

    public static ItemStack getTridentStack(Trident trident) {
        assert tridentStackWrapper != null;
        return tridentStackWrapper.getTridentStack(trident);
    }
}
