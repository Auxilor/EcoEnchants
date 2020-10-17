package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

public final class Marking extends EcoEnchant {
    public Marking() {
        super(
                "marking", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        int ticksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");
        int ticks = ticksPerLevel * level;

        victim.setMetadata("marked", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            victim.removeMetadata("marked", EcoEnchantsPlugin.getInstance());
        }, ticks);
    }

    @EventHandler
    public void onHitWhileMarked(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(!victim.hasMetadata("marked"))
            return;

        event.setDamage(event.getDamage() * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-while-weak"));
    }
}
