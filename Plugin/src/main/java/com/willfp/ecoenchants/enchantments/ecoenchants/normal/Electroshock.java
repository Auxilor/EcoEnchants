package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.util.Lightning;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Electroshock extends EcoEnchant {
    public Electroshock() {
        super(
                new EcoEnchantBuilder("electroshock", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @Override
    public void onDeflect(Player blocker, LivingEntity attacker, int level, EntityDamageByEntityEvent event) {
        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage");

        double finalChance = (chance * level)/100;
        if(NumberUtils.randFloat(0, 1) > finalChance) return;

        Lightning.strike(attacker, damage);
    }
}
