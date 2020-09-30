package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Finality extends EcoEnchant {
    public Finality() {
        super(
                new EcoEnchantBuilder("finality", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        double minhealth = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "minimum-health-per-level");
        if (!(victim.getHealth() <= level * minhealth))
            return;

        event.setDamage(10000); // cba to do this properly
    }
}
