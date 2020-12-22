package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
public class Spring extends EcoEnchant {
    public Spring() {
        super(
                "spring", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onJump(Player player, int level, PlayerMoveEvent event) {
        double multiplier = 0.5 + ((double) (level * level) / 4 - 0.2) / 3;
        player.setVelocity(player.getLocation().getDirection().multiply(multiplier).setY(multiplier));
    }
}
