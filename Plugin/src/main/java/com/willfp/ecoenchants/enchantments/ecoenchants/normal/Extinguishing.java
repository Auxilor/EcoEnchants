package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
public class Extinguishing extends EcoEnchant {
    public Extinguishing() {
        super(
                new EcoEnchantBuilder("extinguishing", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS

    @Override
    public void onDamageWearingArmor(LivingEntity victim, int level, EntityDamageEvent event) {
        if(!event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK))
            return;

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-point");
        if (NumberUtils.randFloat(0, 1) > level * 0.01 * chance)
            return;

        victim.setFireTicks(0);
    }
}
