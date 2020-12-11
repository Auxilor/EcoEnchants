package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.api.BlockBreakWrapper;
import com.willfp.ecoenchants.util.internal.Logger;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Utility class to break a block as if the player had done it manually
 */
public class BlockBreak {
    private static BlockBreakWrapper blockBreakWrapper;

    /**
     * Break a block as a player
     *
     * @param player The player to break the block as
     * @param block  The block to break
     */
    public static void breakBlock(Player player, Block block) {
        blockBreakWrapper.breakBlock(player, block);
    }

    static {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants.nms." + EcoEnchantsPlugin.NMS_VERSION + ".BlockBreak");
            if (BlockBreakWrapper.class.isAssignableFrom(class2)) {
                blockBreakWrapper = (BlockBreakWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            Logger.error("&cYou're running an unsupported server version: " + EcoEnchantsPlugin.NMS_VERSION);
            Bukkit.getPluginManager().disablePlugin(EcoEnchantsPlugin.getInstance());
        }
    }
}
