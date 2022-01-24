package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Instability extends EcoEnchant {

    private static final List<Entity> toProtect = new ArrayList<>();

    public Instability() {
        super(
                "instability", EnchantmentType.SPECIAL
        );
    }

    @EventHandler
    public void onInstabilityExplode(@NotNull final EntityDamageEvent event) {
        if (!toProtect.contains(event.getEntity())) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION && event.getCause()
                != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
            return;
        }

        event.setCancelled(true);
        toProtect.remove(event.getEntity());
    }

    @EventHandler
    public void onInstabilityLand(@NotNull final ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.ARROW) {
            return;
        }

        if (!(event.getEntity().getShooter() instanceof Player player)) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        boolean fire = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire");
        boolean breakblocks = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "break-blocks");

        float power = (float) (0.5 + (level * 0.5));

        if (!AntigriefManager.canCreateExplosion(player, event.getEntity().getLocation())) {
            return;
        }

        if (breakblocks) {
            breakblocks = AntigriefManager.canBreakBlock(player, event.getEntity().getLocation().getWorld().getBlockAt(event.getEntity().getLocation()));
        }

        List<Entity> toAdd = event.getEntity().getNearbyEntities(power, power, power)
                .stream().filter(entity -> entity instanceof LivingEntity && !AntigriefManager.canInjure(player, (LivingEntity) entity))
                .toList();

        toProtect.addAll(
                toAdd
        );

        this.getPlugin().getScheduler().runLater(() -> toProtect.removeAll(toAdd), 20);

        event.getEntity().getWorld().createExplosion(event.getEntity().getLocation().getX(), event.getEntity().getLocation().getY(), event.getEntity().getLocation().getZ(), power, fire, breakblocks);

        event.getEntity().remove();
    }
}
