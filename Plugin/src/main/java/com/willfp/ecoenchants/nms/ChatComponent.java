package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.ChatComponentWrapper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * Utility class to manage chat components
 */
public class ChatComponent {
    private static ChatComponentWrapper chatComponentWrapper;

    @ApiStatus.Internal
    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + EcoEnchantsPlugin.NMS_VERSION + ".ChatComponent");
            if (ChatComponentWrapper.class.isAssignableFrom(class2)) {
                chatComponentWrapper = (ChatComponentWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            chatComponentWrapper = null;
        }
        return chatComponentWrapper != null;
    }

    /**
     * Modify NMS chat component
     * <p>
     * This method will convert any show_item Hover Events using {@link com.willfp.ecoenchants.display.EnchantDisplay#displayEnchantments(ItemStack)}
     *
     * @param object The NMS chat component to modify
     * @return The NMS chat component, having been modified
     */
    public static Object modifyComponent(Object object) {
        assert chatComponentWrapper != null;
        return chatComponentWrapper.modifyComponent(object);
    }
}
