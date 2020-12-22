package com.willfp.eco.core.proxy.proxies;


import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Utility class to break a block as if the player had done it manually
 */
public interface BlockBreakProxy extends AbstractProxy {
    void breakBlock(Player player, Block block);
}
