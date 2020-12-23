package com.willfp.eco.core.proxy.proxies;


import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface BlockBreakProxy extends AbstractProxy {
    /**
     * Break the block as if the player had done it manually.
     *
     * @param player The player to break the block as.
     * @param block  The block to break.
     */
    void breakBlock(@NotNull Player player,
                    @NotNull Block block);
}
