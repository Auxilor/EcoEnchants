package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("deprecation")
public class Graceful extends EcoEnchant {
    public Graceful() {
        super(
                "graceful", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onFall(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isOnGround())
            return;

        if(player.getVelocity().getY() > -1) return;

        if(player.getLocation().clone().add(0, -3, 0).getBlock().getType().equals(Material.AIR))
            return;

        if(!EnchantChecks.boots(player, this)) return;
        int level = EnchantChecks.getBootsLevel(player, this);


        if(!EnchantmentUtils.passedChance(this, level))
            return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 5, false, false, true));
    }
}
