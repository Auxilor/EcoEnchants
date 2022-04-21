package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.core.entities.Entities;
import com.willfp.eco.core.entities.TestableEntity;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings({"unchecked", "unused"})
public class Aiming extends EcoEnchant {
    private final List<TestableEntity> targets = new ArrayList<>();

    public Aiming() {
        super(
                "aiming", EnchantmentType.SPECIAL
        );
        targets.clear();
        targets.addAll(this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "targets").stream().map(
                Entities::lookup
        ).toList());
    }

    @EventHandler
    public void aimingLaunch(@NotNull final ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow arrow)) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance-per-level");

        double distance = level * multiplier;
        double force = arrow.getVelocity().clone().length() / 3;
        force = Math.min(force, 1);

        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "require-full-force") && force < 0.9) {
            return;
        }

        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "scale-on-force")) {
            distance *= force;
        }

        final double finalDistance = distance;

        Runnable runnable = this.getPlugin().getRunnableFactory().create(bukkitRunnable -> {
            List<LivingEntity> nearbyEntities = (List<LivingEntity>) (List<?>) Arrays.asList(arrow.getNearbyEntities(finalDistance, finalDistance, finalDistance).stream()
                    .filter(entity -> entity instanceof LivingEntity)
                    .filter(entity -> this.targets.stream().anyMatch(target -> target.matches(entity)))
                    .map(entity -> (LivingEntity) entity)
                    .filter(entity -> !entity.equals(player))
                    .filter(entity -> !(entity instanceof Enderman))
                    .filter(entity -> AntigriefManager.canInjure(player, entity))
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
                double currentDistance = livingEntity.getLocation().distance(arrow.getLocation());
                if (currentDistance >= dist) {
                    continue;
                }

                dist = currentDistance;
                entity = livingEntity;
            }
            if (entity != null) {
                Vector vector = entity.getEyeLocation().toVector().clone().subtract(arrow.getLocation().toVector()).normalize();
                arrow.setVelocity(vector);
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
            if (arrow.isDead() || arrow.isInBlock() || arrow.isOnGround()) {
                bukkitRunnable.cancel();
            }
            this.getPlugin().getScheduler().run(runnable);
        }).runTaskTimer(3, period);
    }
}
