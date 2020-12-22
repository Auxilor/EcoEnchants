package com.willfp.eco.core.proxy.proxies;


import com.willfp.eco.core.proxy.AbstractProxy;
import org.bukkit.entity.Player;

/**
 * Utility class to get the attack cooldown of a player
 */
public interface CooldownProxy extends AbstractProxy {
    double getAttackCooldown(Player player);
}