package com.willfp.ecoenchants.integrations.antigrief;

import com.willfp.ecoenchants.integrations.Integration;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface AntigriefWrapper extends Integration {
    boolean canBreakBlock(Player player, Block block);

    /**
     * Can player create explosion at location
     * @param player The player
     * @param location The location
     * @return If player can create explosion
     */
    boolean canCreateExplosion(Player player, Location location);

    /**
     * Can player place block
     * @param player The player
     * @param block The block
     * @return If player can place block
     */
    boolean canPlaceBlock(Player player, Block block);

    /**
     * Can player injure living entity
     * @param player The player
     * @param victim The victim
     * @return If player can injure
     */
    boolean canInjure(Player player, LivingEntity victim);
}
