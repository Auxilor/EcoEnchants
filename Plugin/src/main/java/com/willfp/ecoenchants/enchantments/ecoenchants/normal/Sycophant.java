package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class Sycophant extends EcoEnchant {
    public Sycophant() {
        super(
                new EcoEnchantBuilder("sycophant", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDeflect(Player blocker, LivingEntity attacker, int level, EntityDamageByEntityEvent event) {
        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        double amountToHeal = damage * level * multiplier;
        double newHealth = blocker.getHealth() + amountToHeal;
        if (newHealth > blocker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            newHealth = blocker.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        }
        blocker.setHealth(newHealth);
    }
}
