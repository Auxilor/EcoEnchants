package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class Graceful extends EcoEnchant {
    public Graceful() {
        super(
                "graceful", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @EventHandler
    public void onFall(@NotNull final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.isOnGround()) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (player.getVelocity().getY() > -1) {
            return;
        }

        if (player.getLocation().clone().add(0, -3, 0).getBlock().getType().equals(Material.AIR)) {
            return;
        }

        if (!EnchantChecks.boots(player, this)) {
            return;
        }

        int level = EnchantChecks.getBootsLevel(player, this);

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }


        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 20, 5, false, false, true));
    }
}
