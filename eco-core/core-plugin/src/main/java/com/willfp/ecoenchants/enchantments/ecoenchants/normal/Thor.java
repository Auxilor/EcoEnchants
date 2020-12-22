package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.nms.Cooldown;
import com.willfp.ecoenchants.util.LightningUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Thor extends EcoEnchant {
    public Thor() {
        super(
                "thor", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS


    @Override
    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent event) {
        if (attacker instanceof Player) {
            if (Cooldown.getCooldown((Player) attacker) != 1.0f && !this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "allow-not-fully-charged"))
                return;
        }

        if (!EnchantmentUtils.passedChance(this, level))
            return;

        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "lightning-damage");

        LightningUtils.strike(victim, damage);
    }
}
