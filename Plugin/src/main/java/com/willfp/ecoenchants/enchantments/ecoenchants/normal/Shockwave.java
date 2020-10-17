package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.nms.TridentStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
public final class Shockwave extends EcoEnchant {
    public Shockwave() {
        super(
                "shockwave", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

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

        int level = EnchantChecks.getMainhandLevel(player, this);
        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
        damage *= level;
        final double finalDamage = damage;

        new BukkitRunnable() {
            @Override
            public void run() {
                if(entity.isOnGround() || entity.isInBlock() || entity.isDead()) this.cancel();
                entity.getNearbyEntities(1.5, 1.5, 1.5).stream()
                        .filter(entity1 -> entity1 instanceof LivingEntity)
                        .filter(entity1 -> entity1 != player)
                        .filter(entity1 -> !entity1.hasMetadata("shockwaved"))
                        .forEach((mob -> {
                            ((LivingEntity) mob).damage(finalDamage, player);
                            mob.setMetadata("shockwaved", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));
                            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
                                mob.removeMetadata("shockwaved", EcoEnchantsPlugin.getInstance());
                            }, 10);
                        }
                ));
            }
        }.runTaskTimer(EcoEnchantsPlugin.getInstance(), 4, ticks);
    }
}
