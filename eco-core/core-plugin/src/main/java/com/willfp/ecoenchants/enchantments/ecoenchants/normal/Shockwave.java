package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.proxy.proxies.TridentStackProxy;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class Shockwave extends EcoEnchant {
    public Shockwave() {
        super(
                "shockwave", EnchantmentType.NORMAL
        );
    }
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
            item = ProxyUtils.getProxy(TridentStackProxy.class).getTridentStack((Trident) entity);
        }

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int ticks = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "particle-tick-delay");

        int level = EnchantChecks.getMainhandLevel(player, this);
        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
        damage *= level;
        final double finalDamage = damage;

        this.getPlugin().getRunnableFactory().create(runnable -> {
            if (entity.isOnGround() || entity.isInBlock() || entity.isDead()) {
                runnable.cancel();
            }
            entity.getNearbyEntities(1.5, 1.5, 1.5).stream()
                    .filter(entity1 -> entity1 instanceof LivingEntity)
                    .filter(entity1 -> entity1 != player)
                    .filter(entity1 -> !entity1.hasMetadata("shockwaved"))
                    .forEach((mob -> {
                        ((LivingEntity) mob).damage(finalDamage, player);
                        mob.setMetadata("shockwaved", new FixedMetadataValue(this.getPlugin(), true));
                        this.getPlugin().getScheduler().runLater(() -> mob.removeMetadata("shockwaved", this.getPlugin()), 10);
                    }
                    ));
        }).runTaskTimer(4, ticks);
    }
}
