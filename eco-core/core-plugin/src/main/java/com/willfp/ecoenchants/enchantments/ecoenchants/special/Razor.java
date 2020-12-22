package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Cooldown;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Razor extends EcoEnchant {
    public Razor() {
        super(
                "razor", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        double perLevelMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double baseDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-damage");
        double extra = level*perLevelMultiplier + baseDamage;
        if(this.getConfig().getBool((EcoEnchants.CONFIG_LOCATION) + "decrease-if-cooldown")) {
            if(attacker instanceof Player) {
                extra *= Cooldown.getCooldown((Player) attacker);
            }
        }

        event.setDamage(event.getDamage() + extra);
    }
}
