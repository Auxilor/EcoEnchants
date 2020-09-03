package com.willfp.ecoenchants.enchantments;

import com.google.common.util.concurrent.AtomicDouble;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.TridentStack;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Wrapper for Artifact enchantments
 * in order to reduce copying existing code between artifacts.
 */
public abstract class Artifact extends EcoEnchant {
    private final Particle particle;
    private final Particle.DustOptions extra;

    protected Artifact(String key, double version, Particle particle) {
        this(key, version, particle, null);
    }

    protected Artifact(String key, double version, Particle particle, Particle.DustOptions extra) {
        super(new EcoEnchantBuilder(key, EnchantmentType.ARTIFACT, version));
        this.particle = particle;
        this.extra = extra;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(!this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "on-blocks").contains(block.getType().name().toLowerCase())) return;

        if(!EnchantChecks.mainhand(player, this)) return;

        int amount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "amount");
        block.getWorld().spawnParticle(particle, block.getLocation().add(0.5, 0.5, 0.5), amount, 0.4, 0.4, 0.4, 0, extra, false);
    }

    @EventHandler
    public void onElytra(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(!player.isGliding()) return;

        if (!EnchantChecks.chestplate(player, this)) return;

        Location location = player.getLocation();
        Vector direction = player.getLocation().clone().getDirection();

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

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player)) return;
        if(!(event.getEntity() instanceof LivingEntity)) return;

        Player player = (Player) event.getDamager();
        LivingEntity entity = (LivingEntity) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        double radius = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius");

        AtomicDouble yAtomic = new AtomicDouble(0);

        double yDelta = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "y-delta");
        double radiusMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-multiplier");
        double offset = NumberUtils.randFloat(0, 0.75);

        new BukkitRunnable() {
            @Override
            public void run() {
                for(int i = 0; i<3; i++) {
                    if (yAtomic.get() > entity.getHeight()) this.cancel();
                    yAtomic.addAndGet(yDelta);
                    double y = yAtomic.get();
                    double x = radius * Math.cos((y + offset) * radiusMultiplier);
                    double z = radius * Math.sin((y + offset) * radiusMultiplier);
                    Location particleLocation = entity.getLocation();
                    particleLocation.add(x, y, z);
                    entity.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0, extra, false);
                }
            }
        }.runTaskTimer(EcoEnchantsPlugin.getInstance(), 0, 1);
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof AbstractArrow))
            return;

        if(!(event.getEntity().getShooter() instanceof Player)) return;
        Player player = (Player) event.getEntity().getShooter();

        AbstractArrow entity = (AbstractArrow) event.getEntity();
        ItemStack item = player.getInventory().getItemInMainHand();
        if(entity instanceof Trident) {
            item = TridentStack.getTridentStack((Trident) entity);
        }

        if (!EnchantChecks.item(item, this)) return;

        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "particle-tick-delay");

        int noteColor = 0;
        AtomicDouble color = new AtomicDouble(0);
        if(particle.equals(Particle.NOTE)) {
            noteColor = NumberUtils.randInt(0, 24);
            color.set((double) noteColor/24);
        }
        final double finalColor = color.get();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(entity.isOnGround() || entity.isInBlock() || entity.isDead()) this.cancel();
                entity.getLocation().getWorld().spawnParticle(particle, entity.getLocation(), 1, 0, 0, 0, finalColor, extra, true);
            }
        }.runTaskTimer(EcoEnchantsPlugin.getInstance(), 4, ticks);
    }
}
