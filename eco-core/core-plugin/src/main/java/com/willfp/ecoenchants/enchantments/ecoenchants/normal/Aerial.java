package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Aerial extends EcoEnchant {
    public Aerial() {
        super(
                "aerial", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onBowShoot(LivingEntity shooter, Arrow arrow, int level, EntityShootBowEvent event) {
        if (!(event.getProjectile() instanceof Arrow)) return;

        if (shooter.isOnGround()) return;

        event.getProjectile().setMetadata("shot-in-air", new FixedMetadataValue(this.getPlugin(), true));
    }

    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        if (!arrow.hasMetadata("shot-in-air")) return;

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = 1 + (multiplier * level);
        event.setDamage(damage * bonus);
    }
}
