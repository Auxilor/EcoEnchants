package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.metadata.FixedMetadataValue;

public final class Atmospheric extends EcoEnchant {
    public Atmospheric() {
        super(
                new EcoEnchantBuilder("atmospheric", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS

    @Override
    public void onTridentLaunch(LivingEntity shooter, Trident trident, int level, ProjectileLaunchEvent event) {
        if(shooter.isOnGround()) return;

        trident.setMetadata("shot-in-air", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));
    }

    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        if(!trident.hasMetadata("shot-in-air")) return;

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        double reduction = 1 + (multiplier * level);
        event.setDamage(damage * reduction);
    }
}
