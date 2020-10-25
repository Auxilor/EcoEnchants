package com.willfp.ecoenchants.nms;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.nms.API.BlockBreakWrapper;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockBreak {
    private static BlockBreakWrapper blockBreakWrapper;

    public static boolean init() {
        try {
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + EcoEnchantsPlugin.nmsVersion + ".BlockBreak");
            if (BlockBreakWrapper.class.isAssignableFrom(class2)) {
                blockBreakWrapper = (BlockBreakWrapper) class2.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            blockBreakWrapper = null;
        }
        return blockBreakWrapper != null;
    }

    public static void breakBlock(Player player, Block block) {
        assert blockBreakWrapper != null;
        blockBreakWrapper.breakBlock(player, block);
    }
}
