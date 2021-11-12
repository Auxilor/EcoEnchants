package com.willfp.ecoenchants.enchantments.itemtypes;

import com.google.common.util.concurrent.AtomicDouble;
import com.willfp.eco.core.Prerequisite;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Artifact extends EcoEnchant {
    /**
     * The artifact particle.
     */
    private Particle particle;

    /**
     * The extra particle dust options.
     * <p>
     * Used for redstone particles.
     */
    @Nullable
    private Object extra;

    /**
     * Create a new artifact enchantment.
     *
     * @param key           The key name of the enchantment
     * @param prerequisites Optional {@link Prerequisite}s that must be met
     */
    protected Artifact(@NotNull final String key,
                       @NotNull final Prerequisite... prerequisites) {
        super(key, EnchantmentType.ARTIFACT, prerequisites);

        if (!Prerequisite.areMet(prerequisites)) {
            HandlerList.unregisterAll(this); // Prevent events firing
            return;
        }

        this.particle = this.getParticle();
        this.extra = this.getDustOptions();
    }

    /**
     * Get the artifact particle.
     *
     * @return The artifact particle.
     */
    @NotNull
    public abstract Particle getParticle();

    /**
     * The extra particle dust options.
     *
     * @return The dust options.
     */
    @Nullable
    public Object getDustOptions() {
        return null;
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        if (!this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "on-blocks", false).contains(block.getType().name().toLowerCase())) {
            return;
        }

        int amount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "amount");
        block.getWorld().spawnParticle(particle, block.getLocation().add(0.5, 0.5, 0.5), amount, 0.4, 0.4, 0.4, 0, extra, false);
    }

    /**
     * Called on player fly while wearing an elytra.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onElytra(@NotNull final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.isGliding()) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!EnchantChecks.chestplate(player, this)) {
            return;
        }

        this.getPlugin().getScheduler().runAsync(() -> {
            Vector point1 = player.getLocation().getDirection().clone();
            point1.rotateAroundY(Math.toRadians(90));
            point1.multiply(1.2);
            Location location1 = player.getLocation().clone().add(point1);

            Vector point2 = player.getLocation().getDirection().clone();
            point2.rotateAroundY(Math.toRadians(-90));
            point2.multiply(1.2);
            Location location2 = player.getLocation().clone().add(point2);

            player.getWorld().spawnParticle(particle, location1, 1, 0, 0, 0, 0, extra, true);
            player.getWorld().spawnParticle(particle, location2, 1, 0, 0, 0, 0, extra, true);
        });
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        double radius = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius");

        AtomicDouble yAtomic = new AtomicDouble(0);

        double yDelta = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "y-delta");
        double radiusMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-multiplier");
        double offset = NumberUtils.randFloat(0, 0.75);

        boolean doubleHelix = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "use-double-helix");

        this.getPlugin().getRunnableFactory().create(bukkitRunnable -> {
            for (int i = 0; i < 3; i++) {
                if (yAtomic.get() > victim.getHeight()) {
                    bukkitRunnable.cancel();
                }
                yAtomic.addAndGet(yDelta);
                double y = yAtomic.get();
                double x = radius * NumberUtils.fastCos((y + offset) * radiusMultiplier);
                double z = radius * NumberUtils.fastSin((y + offset) * radiusMultiplier);
                Location particleLocation = victim.getLocation();
                particleLocation.add(x, y, z);
                victim.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0, extra, false);
                if (doubleHelix) {
                    Location particleLocation2 = victim.getLocation();
                    particleLocation2.add(-x, y, -z);
                    victim.getWorld().spawnParticle(particle, particleLocation2, 1, 0, 0, 0, 0, extra, false);
                }
            }
        }).runTaskTimerAsynchronously(0, 1);
    }

    @Override
    public void onProjectileLaunch(@NotNull final LivingEntity shooter,
                                   @NotNull final Projectile projectile,
                                   final int level,
                                   @NotNull final ProjectileLaunchEvent event) {
        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "particle-tick-delay");

        int noteColor;
        AtomicDouble color = new AtomicDouble(0);
        if (particle.equals(Particle.NOTE)) {
            noteColor = NumberUtils.randInt(0, 24);
            color.set((double) noteColor / 24);
        }
        final double finalColor = color.get();

        this.getPlugin().getRunnableFactory().create(bukkitRunnable -> {
            if (projectile.isOnGround() || projectile.isDead()) {
                bukkitRunnable.cancel();
            }
            projectile.getLocation().getWorld().spawnParticle(particle, projectile.getLocation(), 1, 0, 0, 0, finalColor, extra, true);
        }).runTaskTimerAsynchronously(4, ticks);
    }
}
