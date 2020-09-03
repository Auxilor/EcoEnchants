package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Force extends EcoEnchant {
    public Force() {
        super(
                new EcoEnchantBuilder("force", EnchantmentType.SPECIAL, Target.Applicable.BOW, 4.01)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow))
            return;

        if(!(((Arrow) event.getDamager()).getShooter() instanceof Player))
            return;

        Arrow arrow = (Arrow) event.getDamager();
        Player player = (Player) ((Arrow) event.getDamager()).getShooter();

        if(!EnchantChecks.arrow(arrow, this))
            return;

        int level = EnchantChecks.getArrowLevel(arrow, this);

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = (multiplier * (level + 6)) + 1;
        event.setDamage(damage * bonus);
    }
}
