package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.FastGetEnchantsWrapper;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Utility class to break a block as if the player had done it manually
 */
public class FastGetEnchants {
    private static FastGetEnchantsWrapper fastGetEnchantsWrapper;

    /**
     * Efficiently get enchantments on an item without instantiating ItemMeta
     *
     * @param item The item to query
     *
     * @return A map of all enchantments, where the integer is the level
     */
    public static Map<Enchantment, Integer> getEnchantsOnItem(ItemStack item) {
        return fastGetEnchantsWrapper.getEnchantmentsOnItem(item);
    }

    static {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants.nms." + EcoEnchantsPlugin.NMS_VERSION + ".FastGetEnchants");
            if (FastGetEnchantsWrapper.class.isAssignableFrom(class2)) {
                fastGetEnchantsWrapper = (FastGetEnchantsWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            Logger.error("&cYou're running an unsupported server version: " + EcoEnchantsPlugin.NMS_VERSION);
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
    }
}
