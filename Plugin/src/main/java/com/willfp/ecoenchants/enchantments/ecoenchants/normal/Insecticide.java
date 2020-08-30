package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Insecticide extends EcoEnchant {
    public Insecticide() {
        super(
                new EcoEnchantBuilder("insecticide", EnchantmentType.NORMAL, Target.Applicable.BOW, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;

        if(!(event.getEntity() instanceof Spider)) return;

        Arrow arrow = (Arrow) event.getDamager();

        if(!EnchantChecks.arrow(arrow, this))
            return;

        int level = EnchantChecks.getArrowLevel(arrow, this);

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = (multiplier * (level + 1)) + 1;
        event.setDamage(damage * bonus);
    }
}
