package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.google.common.collect.Sets;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.UUID;
public class Spring extends EcoEnchant {
    public Spring() {
        super(
                new EcoEnchantBuilder("spring", EnchantmentType.SPECIAL, 5.0)
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
        double multiplier = 0.5 + (double) ((level * level) / 4 - 0.2) / 3;
        player.setVelocity(player.getLocation().getDirection().multiply(multiplier).setY(multiplier));
    }
}
