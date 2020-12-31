package com.willfp.ecoenchants.proxy.proxies;


import com.willfp.eco.util.proxy.AbstractProxy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CooldownProxy extends AbstractProxy {
    /**
     * Get the attack cooldown for a player.
     *
     * @param player The player's attack cooldown.
     * @return A value between 0 and 1, with 1 representing full power.
     */
    double getAttackCooldown(@NotNull Player player);
}
