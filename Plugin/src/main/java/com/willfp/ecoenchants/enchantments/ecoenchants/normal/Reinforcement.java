package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
public class Reinforcement extends EcoEnchant {
    public Reinforcement() {
        super(
                new EcoEnchantBuilder("reinforcement", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onReinforcementHurt(EntityDamageEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;

        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(!EnchantChecks.chestplate(player, this)) return;

        int level = EnchantChecks.getChestplateLevel(player, this);

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "reduction-per-level");
        double multiplier = 1 - ((reduction/100) * level);
        event.setDamage(event.getDamage() * multiplier);
    }
}
