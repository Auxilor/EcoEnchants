package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.TridentStackWrapper;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class to get the {@link ItemStack} of a given {@link Trident}
 */
public class TridentStack {
    private static TridentStackWrapper tridentStackWrapper;

    /**
     * Get the {@link ItemStack} of a given {@link Trident}
     *
     * @param trident The trident to get the ItemStack from
     *
     * @return The ItemStack associated with the trident
     */
    public static ItemStack getTridentStack(Trident trident) {
        return tridentStackWrapper.getTridentStack(trident);
    }

    static {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants.nms." + EcoEnchantsPlugin.NMS_VERSION + ".TridentStack");
            if (TridentStackWrapper.class.isAssignableFrom(class2)) {
                tridentStackWrapper = (TridentStackWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            Logger.error("&cYou're running an unsupported server version: " + EcoEnchantsPlugin.NMS_VERSION);
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
    }
}
