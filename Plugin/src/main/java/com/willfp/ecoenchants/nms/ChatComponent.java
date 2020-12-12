package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.ChatComponentWrapper;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class to manage chat components
 */
public class ChatComponent {
    private static ChatComponentWrapper chatComponentWrapper;

    /**
     * Modify NMS chat component
     * <p>
     * This method will convert any show_item Hover Events using {@link com.willfp.ecoenchants.display.EnchantDisplay#displayEnchantments(ItemStack)}
     *
     * @param object The NMS chat component to modify
     *
     * @return The NMS chat component, having been modified
     */
    public static Object modifyComponent(Object object) {
        return chatComponentWrapper.modifyComponent(object);
    }

    static {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants.nms." + EcoEnchantsPlugin.NMS_VERSION + ".ChatComponent");
            if (ChatComponentWrapper.class.isAssignableFrom(class2)) {
                chatComponentWrapper = (ChatComponentWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            Logger.error("&cYou're running an unsupported server version: " + EcoEnchantsPlugin.NMS_VERSION);
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
    }
}
