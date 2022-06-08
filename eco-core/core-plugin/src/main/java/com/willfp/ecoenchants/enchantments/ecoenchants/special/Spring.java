package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class Spring extends EcoEnchant {
    public Spring() {
        super(
                "spring", EnchantmentType.SPECIAL
        );
    }

    @Override
    public void onDamageWearingArmor(@NotNull final LivingEntity victim,
                                     final int level,
                                     @NotNull final EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onJump(@NotNull final Player player,
                       final int level,
                       @NotNull final PlayerMoveEvent event) {
        if (player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) {
            return;
        }
        AnticheatManager.exemptPlayer(player);

        double multiplier = 0.5 + ((double) (level * level) / 4 - 0.2) / 3;
        player.setVelocity(player.getLocation().getDirection().multiply(multiplier).setY(multiplier));

        this.getPlugin().getScheduler().runLater(() -> AnticheatManager.unexemptPlayer(player), 100);
    }
}
