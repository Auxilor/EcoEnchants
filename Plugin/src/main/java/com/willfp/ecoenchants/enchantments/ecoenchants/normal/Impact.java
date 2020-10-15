package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class Impact extends EcoEnchant {
    public Impact() {
        super(
                new EcoEnchantBuilder("impact", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        event.setDamage(event.getDamage() * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier"));
    }
}
