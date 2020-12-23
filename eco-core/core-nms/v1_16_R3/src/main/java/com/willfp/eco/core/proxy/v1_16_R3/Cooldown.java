package com.willfp.eco.core.proxy.v1_16_R3;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Cooldown implements CooldownProxy {
    @Override
    public double getAttackCooldown(@NotNull final Player player) {
        return player.getAttackCooldown();
    }
}
