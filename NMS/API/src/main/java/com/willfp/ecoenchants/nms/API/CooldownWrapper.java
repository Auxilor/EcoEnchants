package com.willfp.ecoenchants.nms.API;

import org.bukkit.entity.Player;

/**
 * NMS Interface for getting attack cooldown
 */
public interface CooldownWrapper {
    double getAttackCooldown(Player player);
}
