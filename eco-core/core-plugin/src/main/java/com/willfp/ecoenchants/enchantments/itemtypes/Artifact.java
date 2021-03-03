package com.willfp.ecoenchants.enchantments.itemtypes;

import com.google.common.util.concurrent.AtomicDouble;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.TridentUtils;
import com.willfp.eco.util.optional.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
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
    private Particle.DustOptions extra;

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
    public Particle.DustOptions getDustOptions() {
        return null;
    }

    /**
     * Called on block break.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onBreak(@NotNull final BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "on-blocks").contains(block.getType().name().toLowerCase())) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
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

        if (!EnchantChecks.chestplate(player, this)) {
            return;
        }

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
    }

    /**
     * Called when a player hits an entity.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onHit(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }

        Player player = (Player) event.getDamager();
        LivingEntity entity = (LivingEntity) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        double radius = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius");

        AtomicDouble yAtomic = new AtomicDouble(0);

        double yDelta = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "y-delta");
        double radiusMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-multiplier");
        double offset = NumberUtils.randFloat(0, 0.75);


        this.getPlugin().getRunnableFactory().create(bukkitRunnable -> {
            for (int i = 0; i < 3; i++) {
                if (yAtomic.get() > entity.getHeight()) {
                    bukkitRunnable.cancel();
                }
                yAtomic.addAndGet(yDelta);
                double y = yAtomic.get();
                double x = radius * Math.cos((y + offset) * radiusMultiplier);
                double z = radius * Math.sin((y + offset) * radiusMultiplier);
                Location particleLocation = entity.getLocation();
                particleLocation.add(x, y, z);
                entity.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0, extra, false);
                if (EnchantChecks.getMainhandLevel(player,this)>=2) {
                    Location particleLocation2 = entity.getLocation();
                    particleLocation2.add(-x, y, -z);
                    entity.getWorld().spawnParticle(particle, particleLocation2, 1, 0, 0, 0, 0, extra, false);
                }
                if (EnchantChecks.getMainhandLevel(player,this)>=3) {
                    Location particleLocation3 = entity.getLocation();
                    Location particleLocation4 = entity.getLocation();
                    particleLocation3.add(-x, y, z);
                    particleLocation4.add(x, y, -z);
                    entity.getWorld().spawnParticle(particle, particleLocation3, 1, 0, 0, 0, 0, extra, false);
                    entity.getWorld().spawnParticle(particle, particleLocation4, 1, 0, 0, 0, 0, extra, false);
                }
                if (EnchantChecks.getMainhandLevel(player,this)>=4) {
                    Location particleLocation5 = entity.getLocation();
                    Location particleLocation6 = entity.getLocation();
                    Location particleLocation7 = entity.getLocation();
                    Location particleLocation8 = entity.getLocation();
                    particleLocation5.add(x, -y, z);
                    particleLocation6.add(-x, -y, -z);
                    particleLocation7.add(-x, -y, z);
                    particleLocation8.add(x, -y, -z);
                    entity.getWorld().spawnParticle(particle, particleLocation5, 1, 0, 0, 0, 0, extra, false);
                    entity.getWorld().spawnParticle(particle, particleLocation6, 1, 0, 0, 0, 0, extra, false);
                    entity.getWorld().spawnParticle(particle, particleLocation7, 1, 0, 0, 0, 0, extra, false);
                    entity.getWorld().spawnParticle(particle, particleLocation8, 1, 0, 0, 0, 0, extra, false);
                }
                if (EnchantChecks.getMainhandLevel(player,this)<4) {
                    for (int j = EnchantChecks.getMainhandLevel(player,this)-4; j>0; i-=1) {
                        Location particleLocation5 = entity.getLocation();
                        Location particleLocation6 = entity.getLocation();
                        Location particleLocation7 = entity.getLocation();
                        Location particleLocation8 = entity.getLocation();
                        particleLocation5.add(x*j, -y, z*j);
                        particleLocation6.add(-x*j, -y, -z*j);
                        particleLocation7.add(-x*j, -y, z*j);
                        particleLocation8.add(x*j, -y, -z*j);
                        entity.getWorld().spawnParticle(particle, particleLocation5, 1, 0, 0, 0, 0, extra, false);
                        entity.getWorld().spawnParticle(particle, particleLocation6, 1, 0, 0, 0, 0, extra, false);
                        entity.getWorld().spawnParticle(particle, particleLocation7, 1, 0, 0, 0, 0, extra, false);
                        entity.getWorld().spawnParticle(particle, particleLocation8, 1, 0, 0, 0, 0, extra, false);
                    }
                }
            }
        }).runTaskTimer(0, 1);
    }

    /**
     * Called on projectile launch.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void onShoot(@NotNull final ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow)) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity().getShooter();

        AbstractArrow entity = (AbstractArrow) event.getEntity();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (entity instanceof Trident) {
            item = TridentUtils.getItemStack((Trident) entity);
        }

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "particle-tick-delay");

        int noteColor;
        AtomicDouble color = new AtomicDouble(0);
        if (particle.equals(Particle.NOTE)) {
            noteColor = NumberUtils.randInt(0, 24);
            color.set((double) noteColor / 24);
        }
        final double finalColor = color.get();

        this.getPlugin().getRunnableFactory().create(bukkitRunnable -> {
            if (entity.isOnGround() || entity.isInBlock() || entity.isDead()) {
                bukkitRunnable.cancel();
            }
            entity.getLocation().getWorld().spawnParticle(particle, entity.getLocation(), 1, 0, 0, 0, finalColor, extra, true);
        }).runTaskTimer(4, ticks);
    }
}
