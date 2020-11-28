package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.TridentStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
public class Splash extends EcoEnchant {
    public Splash() {
        super(
                "splash", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onSplashLand(ProjectileHitEvent event) {
        if (event.getEntityType() != EntityType.TRIDENT)
            return;

        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        if (!(event.getEntity() instanceof Trident)) return;



        Trident trident = (Trident) event.getEntity();
        Player player = (Player) event.getEntity().getShooter();

        ItemStack item = TridentStack.getTridentStack(trident);

        if (!EnchantChecks.item(item, this)) return;

        int level = EnchantChecks.getItemLevel(item, this);

        double radius = level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-multiplier");
        double damage = level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");

        for (Entity e : trident.getNearbyEntities(radius, radius, radius)) {
            if(e.hasMetadata("NPC")) continue;

            if (!(e instanceof LivingEntity)) continue;
            LivingEntity entity = (LivingEntity) e;
            if(e.equals(player)) continue;

            Bukkit.getPluginManager().callEvent(new EntityDamageByEntityEvent(trident, entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage));

            if(!AntigriefManager.canInjure(player, entity)) continue;
            entity.damage(damage, trident);
        }
    }
}