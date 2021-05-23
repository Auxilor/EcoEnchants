package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Carve extends EcoEnchant {
    public Carve() {
        super(
                "carve", EnchantmentType.SPECIAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull final LivingEntity attacker,
                              @NotNull final LivingEntity victim,
                              final int level,
                              @NotNull final EntityDamageByEntityEvent event) {
        if (victim.hasMetadata("cleaved")) {
            return;
        }

        double damagePerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-percentage-per-level") * 0.01;
        double radiusPerLevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-per-level");
        final double damage = damagePerLevel * level * event.getDamage();
        final double radius = radiusPerLevel * level;

        if (!EnchantmentUtils.isFullyChargeIfRequired(this, attacker)) {
            return;
        }

        victim.getNearbyEntities(radius, radius, radius).stream()
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> !entity.equals(attacker))
                .forEach(entity -> {
                    entity.setMetadata("cleaved", this.getPlugin().getMetadataValueFactory().create(true));
                    this.getPlugin().getScheduler().run(() -> ((LivingEntity) entity).damage(damage, attacker));
                    this.getPlugin().getScheduler().runLater(() -> entity.removeMetadata("cleaved", this.getPlugin()), 20);
                });
    }
}
