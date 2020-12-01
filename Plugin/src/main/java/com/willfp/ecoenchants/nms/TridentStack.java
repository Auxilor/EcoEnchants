package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.API.TridentStackWrapper;
import org.bukkit.entity.Trident;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * Utility class to get the {@link ItemStack} of a given {@link Trident}
 */
public class TridentStack {
    private static TridentStackWrapper tridentStackWrapper;

    @ApiStatus.Internal
    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + EcoEnchantsPlugin.NMS_VERSION + ".TridentStack");
            if (TridentStackWrapper.class.isAssignableFrom(class2)) {
                tridentStackWrapper = (TridentStackWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            tridentStackWrapper = null;
        }
        return tridentStackWrapper != null;
    }

    /**
     * Get the {@link ItemStack} of a given {@link Trident}
     *
     * @param trident The trident to get the ItemStack from
     * @return The ItemStack associated with the trident
     */
    public static ItemStack getTridentStack(Trident trident) {
        assert tridentStackWrapper != null;
        return tridentStackWrapper.getTridentStack(trident);
    }
}
