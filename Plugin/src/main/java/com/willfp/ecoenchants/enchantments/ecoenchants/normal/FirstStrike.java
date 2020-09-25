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
public class FirstStrike extends EcoEnchant {
    public FirstStrike() {
        super(
                new EcoEnchantBuilder("first_strike", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (!(victim.getHealth() == victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()))
            return;

        double damagemultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        event.setDamage(event.getDamage() * ((level * damagemultiplier) + 1));
    }
}
