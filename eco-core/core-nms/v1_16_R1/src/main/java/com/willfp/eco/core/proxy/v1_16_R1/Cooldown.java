package com.willfp.eco.core.proxy.v1_16_R1;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import org.bukkit.entity.Player;

public class Cooldown implements CooldownProxy {
    @Override
    public double getAttackCooldown(Player player) {
        return player.getAttackCooldown();
    }
}
