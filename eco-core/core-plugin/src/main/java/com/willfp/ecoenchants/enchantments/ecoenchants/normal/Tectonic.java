package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Tectonic extends EcoEnchant {
    public Tectonic() {
        super(
                "tectonic", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onFallDamage(@NotNull final LivingEntity faller,
                             final int level,
                             @NotNull final EntityDamageEvent event) {

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        int radius = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-radius") + (this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-radius") * level - 1);
        int damage = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-damage") + (this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-damage") * level - 1);


        Collection<Entity> entities = faller.getWorld().getNearbyEntities(faller.getLocation(), radius, 2, radius);

        for (Entity entity : entities) {
            if (entity.equals(faller)) {
                continue;
            }
            if (!(entity instanceof LivingEntity)) {
                continue;
            }
            entity.teleport(entity.getLocation().add(0, 0.3, 0));
            if (faller instanceof Player && !AntigriefManager.canInjure((Player) faller, (LivingEntity) entity)) {
                return;
            }
            ((LivingEntity) entity).damage(damage);
        }
    }

}
