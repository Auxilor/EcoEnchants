package com.willfp.ecoenchants.v1_16_R1;

import com.willfp.ecoenchants.nms.API.CooldownWrapper;
import org.bukkit.entity.Player;

public class Cooldown implements CooldownWrapper {
    @Override
    public double getAttackCooldown(Player player) {
        return player.getAttackCooldown();
    }
}
