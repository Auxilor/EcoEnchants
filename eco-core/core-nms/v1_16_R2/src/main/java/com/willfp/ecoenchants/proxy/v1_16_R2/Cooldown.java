package com.willfp.ecoenchants.proxy.v1_16_R2;

import com.willfp.ecoenchants.proxy.proxies.CooldownProxy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Cooldown implements CooldownProxy {
    @Override
    public double getAttackCooldown(@NotNull final Player player) {
        return player.getAttackCooldown();
    }
}
