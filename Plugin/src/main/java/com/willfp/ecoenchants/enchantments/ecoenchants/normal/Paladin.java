package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Paladin extends EcoEnchant {
    public Paladin() {
        super(
                new EcoEnchantBuilder("paladin", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(!(attacker.getVehicle() instanceof Horse)) return;

        event.setDamage(event.getDamage() * ((level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level")) + 1));
    }
}
