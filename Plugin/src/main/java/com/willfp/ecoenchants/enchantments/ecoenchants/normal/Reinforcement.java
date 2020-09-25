package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
public class Reinforcement extends EcoEnchant {
    public Reinforcement() {
        super(
                new EcoEnchantBuilder("reinforcement", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "reduction-per-level");
        double multiplier = 1 - ((reduction/100) * level);
        event.setDamage(event.getDamage() * multiplier);
    }
}
