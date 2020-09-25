package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
public class Marksman extends EcoEnchant {
    public Marksman() {
        super(
                new EcoEnchantBuilder("marksman", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onBowShoot(LivingEntity shooter, Arrow arrow, int level, EntityShootBowEvent event) {
        arrow.setGravity(false);

        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "remove-arrow-after-ticks");

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!arrow.isOnGround()) {
                    arrow.remove();
                }
            }
        }.runTaskLater(EcoEnchantsPlugin.getInstance(), ticks);
    }
}
