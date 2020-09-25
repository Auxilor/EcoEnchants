package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Deflection extends EcoEnchant {
    public Deflection() {
        super(
                new EcoEnchantBuilder("deflection", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onDeflect(Player blocker, LivingEntity attacker, int level, EntityDamageByEntityEvent event) {
        double perlevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-deflected-per-level");
        double damagePercent = (perlevel/100) * level;
        double damage = event.getDamage() * damagePercent;

        attacker.damage(damage, attacker);
    }
}
