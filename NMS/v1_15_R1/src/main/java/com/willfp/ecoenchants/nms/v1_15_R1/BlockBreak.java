package com.willfp.ecoenchants.nms.v1_15_R1;

import com.willfp.ecoenchants.nms.api.BlockBreakWrapper;
import net.minecraft.server.v1_15_R1.BlockPosition;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockBreak implements BlockBreakWrapper {
    @Override
    public void breakBlock(Player player, Block block) {
        ((CraftPlayer)player).getHandle().playerInteractManager.breakBlock(new BlockPosition(block.getX(), block.getY(), block.getZ()));
    }
}
