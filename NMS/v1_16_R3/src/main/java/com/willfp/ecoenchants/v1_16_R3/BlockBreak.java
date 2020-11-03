package com.willfp.ecoenchants.v1_16_R3;

import com.willfp.ecoenchants.nms.API.BlockBreakWrapper;
import net.minecraft.server.v1_16_R3.BlockPosition;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class BlockBreak implements BlockBreakWrapper {
    @Override
    public void breakBlock(Player player, Block block) {
        ((CraftPlayer)player).getHandle().playerInteractManager.breakBlock(new BlockPosition(block.getX(), block.getY(), block.getZ()));
    }
}
