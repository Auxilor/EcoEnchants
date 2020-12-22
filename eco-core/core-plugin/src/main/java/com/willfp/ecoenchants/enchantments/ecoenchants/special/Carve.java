package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.core.proxy.proxies.CooldownProxy;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Carve extends EcoEnchant {
    public Carve() {
        super(
                "carve", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(victim.hasMetadata("carved"))
            return;

        double damagePerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-percentage-per-level") * 0.01;
        double radiusPerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-per-level");
        final double damage = damagePerLevel * level * event.getDamage();
        final double radius = radiusPerLevel * level;

        if(attacker instanceof Player && ProxyUtils.getProxy(CooldownProxy.class).getAttackCooldown((Player) attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged")) {
            return;
        }

        victim.getNearbyEntities(radius, radius, radius).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> !entity.equals(attacker))
                .forEach(entity -> {
                    entity.setMetadata("carved", new FixedMetadataValue(this.plugin, true));
                    ((LivingEntity) entity).damage(damage, attacker);
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> entity.removeMetadata("carved", this.plugin), 20);
                });
    }
}
