package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.API.ChatComponentWrapper;
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
     * Modify chat component
     */
    public static Object modifyComponent(Object object) {
        assert chatComponentWrapper != null;
        return chatComponentWrapper.modifyComponent(object);
    }
}
