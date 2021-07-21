package com.willfp.ecoenchants.precision;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.GameMode;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")
public class Precision extends EcoEnchant {
    public Precision() {
        super(
                "precision", EnchantmentType.SPECIAL
        );
    }

    @EventHandler
    public void aimingLaunch(@NotNull final ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }

        if (!(event.getEntity() instanceof Trident trident)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        ItemStack itemStack = trident.getItem();
        if (!EnchantChecks.item(itemStack, this)) {
            return;
        }
        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level");

        final double finalDistance = level * multiplier;
        Runnable runnable = this.getPlugin().getRunnableFactory().create(bukkitRunnable -> {
            List<LivingEntity> nearbyEntities = (List<LivingEntity>) (List<?>) Arrays.asList(trident.getNearbyEntities(finalDistance, finalDistance, finalDistance).stream()
                    .filter(entity -> entity instanceof LivingEntity)
                    .filter(entity -> !entity.equals(player))
                    .filter(entity -> !(entity instanceof Enderman))
                    .filter(entity -> {
                        if (entity instanceof Player) {
                            return ((Player) entity).getGameMode().equals(GameMode.SURVIVAL) || ((Player) entity).getGameMode().equals(GameMode.ADVENTURE);
                        }
                        return true;
                    }).toArray());
            if (nearbyEntities.isEmpty()) {
                return;
            }
            LivingEntity entity = nearbyEntities.get(0);
            double dist = Double.MAX_VALUE;
            for (LivingEntity livingEntity : nearbyEntities) {
                double currentDistance = livingEntity.getLocation().distance(trident.getLocation());
                if (currentDistance >= dist) {
                    continue;
                }

                dist = currentDistance;
                entity = livingEntity;
            }
            if (entity != null) {
                Vector vector = entity.getEyeLocation().toVector().clone().subtract(trident.getLocation().toVector()).normalize();
                trident.setVelocity(vector);
            }
        });

        final int period = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "check-ticks");
        final int checks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "checks-per-level") * level;
        AtomicInteger checksPerformed = new AtomicInteger(0);

        this.getPlugin().getRunnableFactory().create(bukkitRunnable -> {
            checksPerformed.addAndGet(1);
            if (checksPerformed.get() > checks) {
                bukkitRunnable.cancel();
            }
            if (trident.isDead() || trident.isInBlock() || trident.isOnGround()) {
                bukkitRunnable.cancel();
            }
            this.getPlugin().getScheduler().run(runnable);
        }).runTaskTimer(3, period);
    }
}
