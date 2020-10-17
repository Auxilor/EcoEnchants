package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public final class WaterAspect extends EcoEnchant {
    public WaterAspect() {
        super(
                "water_aspect", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if(!(victim instanceof Blaze || victim instanceof MagmaCube || victim instanceof Enderman))
            return;

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        double damageMultiplier = (level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}