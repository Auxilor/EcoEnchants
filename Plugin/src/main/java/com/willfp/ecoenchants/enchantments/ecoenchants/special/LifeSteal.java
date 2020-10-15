package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class LifeSteal extends EcoEnchant {
    public LifeSteal() {
        super(
                new EcoEnchantBuilder("life_steal", EnchantmentType.SPECIAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        double amountToHeal = damage * level * multiplier;
        double newHealth = attacker.getHealth() + amountToHeal;
        if (newHealth > attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            newHealth = attacker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }
        attacker.setHealth(newHealth);
    }
}
