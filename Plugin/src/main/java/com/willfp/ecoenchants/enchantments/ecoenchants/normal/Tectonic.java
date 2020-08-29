package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;
public class Tectonic extends EcoEnchant {
    public Tectonic() {
        super(
                new EcoEnchantBuilder("tectonic", EnchantmentType.NORMAL, Target.Applicable.BOOTS, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onTecFall(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            return;

        if (!EnchantChecks.boots(player, this)) return;

        int radius = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-radius") + (this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-radius") * EnchantChecks.getBootsLevel(player, this) - 1);
        int damage = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-damage") + (this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-damage") * EnchantChecks.getBootsLevel(player, this) - 1);


        Collection<Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), radius, 2, radius);

        for (Entity entity : entities) {
            if (entity.equals(player))
                continue;
            entity.teleport(entity.getLocation().add(0, 0.3, 0));
            ((LivingEntity) entity).damage(damage);
        }
    }

}
