package com.willfp.eco.core.proxy.v1_16_R1;

import com.willfp.eco.core.proxy.proxies.BlockBreakProxy;
import net.minecraft.server.v1_16_R1.BlockPosition;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockBreak implements BlockBreakProxy {
    @Override
    public void breakBlock(Player player, Block block) {
        ((CraftPlayer) player).getHandle().playerInteractManager.breakBlock(new BlockPosition(block.getX(), block.getY(), block.getZ()));
    }
}
