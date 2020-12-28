package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class Marking extends EcoEnchant {
    public Marking() {
        super(
                "marking", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, @NotNull Arrow arrow, int level, @NotNull EntityDamageByEntityEvent event) {
        int ticksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");
        int ticks = ticksPerLevel * level;

        victim.setMetadata("marked", new FixedMetadataValue(this.getPlugin(), true));

        this.getPlugin().getScheduler().runLater(() -> {
            victim.removeMetadata("marked", this.getPlugin());
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
