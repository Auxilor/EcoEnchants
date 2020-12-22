package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.LightningUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Electroshock extends EcoEnchant {
    public Electroshock() {
        super(
                "electroshock", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @Override
    public void onDeflect(Player blocker, LivingEntity attacker, int level, EntityDamageByEntityEvent event) {
        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage");

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        LightningUtils.strike(attacker, damage);
    }
}
