package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.EqualIfOver;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class Aiming extends EcoEnchant {
    public Aiming() {
        super(
                new EcoEnchantBuilder("aiming", EnchantmentType.SPECIAL, Target.Applicable.BOW, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void aimingLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity().getShooter() instanceof Player))
            return;

        if(!(event.getEntity() instanceof Arrow))
            return;

        if(event.isCancelled()) return;

        Player player = (Player) event.getEntity().getShooter();
        Arrow arrow = (Arrow) event.getEntity();

        if(!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level");

        double distance = level * multiplier;
        double force = arrow.getVelocity().clone().length() / 3;
        force = EqualIfOver.equalIfOver(force, 1);

        if(this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "require-full-force")) {
            if(force < 0.9) return;
        }

        if(this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "scale-on-force")) {
            distance *= force;
        }

        final double finalDistance = distance;
        Runnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                List<LivingEntity> nearbyEntities = (List<LivingEntity>) (List<?>) Arrays.asList(arrow.getNearbyEntities(finalDistance, finalDistance, finalDistance).stream()
                        .filter(entity -> entity instanceof LivingEntity)
                        .filter(entity -> !entity.equals(player))
                        .filter(entity -> !(entity instanceof Enderman))
                        .filter(entity -> {
                            if(entity instanceof Player) {
                                return ((Player) entity).getGameMode().equals(GameMode.SURVIVAL) || ((Player) entity).getGameMode().equals(GameMode.ADVENTURE);
                            }
                            return true;
                        }).toArray());
                if(nearbyEntities.isEmpty()) return;
                LivingEntity entity = nearbyEntities.get(0);
                double distance = Double.MAX_VALUE;
                for(LivingEntity livingEntity : nearbyEntities) {
                    double currentDistance = livingEntity.getLocation().distance(arrow.getLocation());
                    if(currentDistance >= distance) continue;

                    distance = currentDistance;
                    entity = livingEntity;
                }
                if(entity != null) {
                    Vector vector = entity.getEyeLocation().toVector().clone().subtract(arrow.getLocation().toVector()).normalize();
                    arrow.setVelocity(vector);
                }
            }
        };

        new BukkitRunnable() {
            @Override
            public void run() {
                if(arrow.isDead() || arrow.isInBlock() || arrow.isOnGround()) this.cancel();
                Bukkit.getScheduler().runTask(EcoEnchantsPlugin.getInstance(), runnable);
            }
        }.runTaskTimer(EcoEnchantsPlugin.getInstance(), 3, 5);
    }
}
