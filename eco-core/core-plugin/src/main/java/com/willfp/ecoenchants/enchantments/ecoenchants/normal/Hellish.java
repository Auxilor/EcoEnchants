package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Hellish extends EcoEnchant {
    public Hellish() {
        super(
                "hellish", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        if(!attacker.getWorld().getEnvironment().equals(World.Environment.NETHER))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }
}
