package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Goliath extends EcoEnchant {
    public Goliath() {
        super(
                new EcoEnchantBuilder("goliath", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (victim.getHealth() <= attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
            return;

        double timesMoreHealth = victim.getHealth() / attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        event.setDamage(event.getDamage() * ((level * multiplier * timesMoreHealth) + 1));
    }
}
