package com.willfp.eco.util.integrations.antigrief;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class AntigriefManager {
    /**
     * Registered antigriefs.
     */
    private final Set<AntigriefWrapper> registered = new HashSet<>();

    /**
     * Register a new AntiGrief/Land Management integration.
     *
     * @param antigrief The integration to register.
     */
    public void register(@NotNull final AntigriefWrapper antigrief) {
        registered.add(antigrief);
    }

    /**
     * Can player break block.
     *
     * @param player The player.
     * @param block  The block.
     * @return If player can break block.
     */
    public boolean canBreakBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        return registered.stream().allMatch(antigriefWrapper -> antigriefWrapper.canBreakBlock(player, block));
    }

    /**
     * Can player create explosion at location.
     *
     * @param player   The player.
     * @param location The location.
     * @return If player can create explosion.
     */
    public boolean canCreateExplosion(@NotNull final Player player,
                                      @NotNull final Location location) {
        return registered.stream().allMatch(antigriefWrapper -> antigriefWrapper.canCreateExplosion(player, location));
    }

    /**
     * Can player place block.
     *
     * @param player The player.
     * @param block  The block.
     * @return If player can place block.
     */
    public boolean canPlaceBlock(@NotNull final Player player,
                                 @NotNull final Block block) {
        return registered.stream().allMatch(antigriefWrapper -> antigriefWrapper.canPlaceBlock(player, block));
    }

    /**
     * Can player injure living entity.
     *
     * @param player The player.
     * @param victim The victim.
     * @return If player can injure.
     */
    public boolean canInjure(@NotNull final Player player,
                             @NotNull final LivingEntity victim) {
        return registered.stream().allMatch(antigriefWrapper -> antigriefWrapper.canInjure(player, victim));
    }
}
