package com.willfp.eco.core.proxy.v1_15_R1;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import net.minecraft.server.v1_15_R1.EntityHuman;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class Cooldown implements CooldownProxy {
    @Override
    public double getAttackCooldown(@NotNull final Player player) {
        EntityHuman entityHuman = ((CraftPlayer) player).getHandle();
        return entityHuman.s(0);
    }
}
