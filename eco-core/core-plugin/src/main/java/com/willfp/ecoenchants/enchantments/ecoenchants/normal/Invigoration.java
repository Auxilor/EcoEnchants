package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
public class Invigoration extends EcoEnchant {
    public Invigoration() {
        super(
                "invigoration", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onInvigorationHurt(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if (player.getHealth() > this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "below-health"))
            return;

        int totalInvigorationPoints = EnchantChecks.getArmorPoints(player, this, 0);
        if(this.getDisabledWorlds().contains(player.getWorld())) return;

        if (totalInvigorationPoints == 0)
            return;

        double damageReduction = totalInvigorationPoints * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "reduction-multiplier") * 0.01;
        damageReduction += 1;
        event.setDamage(event.getDamage() * damageReduction);
    }

    @EventHandler
    public void onInvigorationDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        Player player = (Player) event.getDamager();

        if (player.getHealth() > this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "below-health"))
            return;

        int totalInvigorationPoints = EnchantChecks.getArmorPoints(player, this, 0);
        if(this.getDisabledWorlds().contains(player.getWorld())) return;

        if (totalInvigorationPoints == 0)
            return;

        double damageBonus = totalInvigorationPoints * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier") * 0.01;
        damageBonus += 1;
        event.setDamage(event.getDamage() * damageBonus);
    }

}
