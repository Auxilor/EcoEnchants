package com.willfp.ecoenchants.precision;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.nms.TridentStack;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")
public final class Precision extends EcoEnchant {
    public Precision() {
        super(
                "precision", EnchantmentType.SPECIAL, PrecisionMain.class
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void aimingLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        if(!(event.getEntity() instanceof Trident))
            return;

        if(event.isCancelled()) return;

        Player player = (Player) event.getEntity().getShooter();
        Trident trident = (Trident) event.getEntity();

        ItemStack itemStack = TridentStack.getTridentStack(trident);
        if (!EnchantChecks.item(itemStack, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level");

        final double finalDistance = level * multiplier;
        Runnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                List<LivingEntity> nearbyEntities = (List<LivingEntity>)(List<?>) Arrays.asList(trident.getNearbyEntities(finalDistance, finalDistance, finalDistance).stream()
                        .filter(entity -> entity instanceof LivingEntity)
                        .filter(entity -> !entity.equals(player))
                        .filter(entity -> !(entity instanceof Enderman))
                        .filter(entity -> {
                            if (entity instanceof Player) {
                                return ((Player) entity).getGameMode().equals(GameMode.SURVIVAL) || ((Player) entity).getGameMode().equals(GameMode.ADVENTURE);
                            }
                            return true;
                        }).toArray());
                if(nearbyEntities.isEmpty()) return;
                LivingEntity entity = nearbyEntities.get(0);
                double distance = Double.MAX_VALUE;
                for(LivingEntity livingEntity : nearbyEntities) {
                    double currentDistance = livingEntity.getLocation().distance(trident.getLocation());
                    if(currentDistance >= distance) continue;

                    distance = currentDistance;
                    entity = livingEntity;
                }
                if(entity != null) {
                    Vector vector = entity.getEyeLocation().toVector().clone().subtract(trident.getLocation().toVector()).normalize();
                    trident.setVelocity(vector);
                }
            }
        };

        final int period = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "check-ticks");
        final int checks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "checks-per-level") * level;
        AtomicInteger checksPerformed = new AtomicInteger(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                checksPerformed.addAndGet(1);
                if(checksPerformed.get() > checks) this.cancel();
                if(trident.isDead() || trident.isInBlock() || trident.isOnGround()) this.cancel();
                Bukkit.getScheduler().runTask(EcoEnchantsPlugin.getInstance(), runnable);
            }
        }.runTaskTimer(EcoEnchantsPlugin.getInstance(), 3, period);
    }
}
