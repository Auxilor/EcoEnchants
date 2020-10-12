package com.willfp.ecoenchants.v1_15_R1;

import com.willfp.ecoenchants.nms.API.CooldownWrapper;
import net.minecraft.server.v1_15_R1.EntityHuman;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Cooldown implements CooldownWrapper {
    @Override
    public double getAttackCooldown(Player player) {
        EntityHuman entityHuman = ((CraftPlayer) player).getHandle();
        return entityHuman.s(0);
    }
}
