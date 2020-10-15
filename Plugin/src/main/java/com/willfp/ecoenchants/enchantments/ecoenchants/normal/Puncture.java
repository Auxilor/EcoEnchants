package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Trident;
import org.bukkit.entity.Turtle;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class Puncture extends EcoEnchant {
    public Puncture() {
        super(
                new EcoEnchantBuilder("puncture", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onTridentDamage(LivingEntity attacker, LivingEntity victim, Trident trident, int level, EntityDamageByEntityEvent event) {
        if(!(victim instanceof Turtle || victim instanceof Shulker))
            return;

        double perLevelDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-more-per-level");

        double totalDamagePercent = (100 + (perLevelDamage * level))/100;

        event.setDamage(event.getDamage() * totalDamagePercent);
    }
}
