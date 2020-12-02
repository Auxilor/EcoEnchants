package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.API.JsonStackWrapper;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * Utility class to read/write NBTTagCompounds through json
 */
public class JsonStack {
    private static JsonStackWrapper jsonStackWrapper;

    @ApiStatus.Internal
    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + EcoEnchantsPlugin.NMS_VERSION + ".JsonStack");
            if (JsonStackWrapper.class.isAssignableFrom(class2)) {
                jsonStackWrapper = (JsonStackWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonStackWrapper = null;
        }
        return jsonStackWrapper != null;
    }

    /**
     * Get the NBTTagCompound of an item as json
     *
     * @param itemStack The item to query
     * @return The NBTTagCompound of the item as json
     */
    public static String toJson(ItemStack itemStack) {
        assert jsonStackWrapper != null;
        return jsonStackWrapper.toJson(itemStack);
    }

    /**
     * Create an ItemStack from NBTTagCompound json
     * @param jsonTag The tag of the item
     * @param id The fully qualified material ID
     * @return The reconstructed ItemStack
     */
    public static ItemStack fromJson(String jsonTag, String id) {
        assert jsonStackWrapper != null;
        return jsonStackWrapper.getFromTag(jsonTag, id);
    }
}
