package com.willfp.ecoenchants.integrations.antigrief;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class AntigriefManager {
    private static final Set<AntigriefWrapper> antigriefs = new HashSet<>();

    public static void register(AntigriefWrapper antigrief) {
        antigriefs.add(antigrief);
    }

    /**
     * Can player break block
     * @param player The player
     * @param block The block
     * @return If player can break block
     */
    public static boolean canBreakBlock(Player player, Block block) {
        return antigriefs.stream().allMatch(antigriefWrapper -> antigriefWrapper.canBreakBlock(player, block));
    }

    /**
     * Can player create explosion at location
     * @param player The player
     * @param location The location
     * @return If player can create explosion
     */
    public static boolean canCreateExplosion(Player player, Location location) {
        return antigriefs.stream().allMatch(antigriefWrapper -> antigriefWrapper.canCreateExplosion(player, location));
    }

    /**
     * Can player place block
     * @param player The player
     * @param block The block
     * @return If player can place block
     */
    public static boolean canPlaceBlock(Player player, Block block) {
        return antigriefs.stream().allMatch(antigriefWrapper -> antigriefWrapper.canPlaceBlock(player, block));
    }

    /**
     * Can player injure living entity
     * @param player The player
     * @param victim The victim
     * @return If player can injure
     */
    public static boolean canInjure(Player player, LivingEntity victim) {
        return antigriefs.stream().allMatch(antigriefWrapper -> antigriefWrapper.canInjure(player, victim));
    }
}
