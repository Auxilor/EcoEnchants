package com.willfp.ecoenchants.nms;


import com.willfp.ecoenchants.API.BlockBreakWrapper;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.codehaus.plexus.util.reflection.ReflectionManager;

public class BlockBreak {
    private static BlockBreakWrapper blockBreakWrapper;

    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static boolean init() {
        try {
            ReflectionManager.accessClass("com.willfp.ecoenchants." + version + ".BlockBreak");
            final Class<?> class2 = Class.forName("com.willfp.ecoenchants." + version + ".BlockBreak");
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
