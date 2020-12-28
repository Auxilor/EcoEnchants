package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class Weakening extends EcoEnchant {
    public Weakening() {
        super(
                "weakening", EnchantmentType.NORMAL
        );
    }

    @Override
    public void onMeleeAttack(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, int level, @NotNull EntityDamageByEntityEvent event) {
        int ticksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");
        int ticks = ticksPerLevel * level;

        victim.setMetadata("weak", new FixedMetadataValue(this.getPlugin(), true));

        this.getPlugin().getScheduler().runLater(() -> {
            victim.removeMetadata("weak", this.getPlugin());
        }, ticks);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity))
            return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(!victim.hasMetadata("weak"))
            return;

        event.setDamage(event.getDamage() * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-while-weak"));
    }
}
