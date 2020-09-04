package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Diverse extends EcoEnchant {
    public Diverse() {
        super(
                new EcoEnchantBuilder("diverse", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if(!victim.getInventory().getItemInMainHand().getType().toString().endsWith("_SWORD"))
            return;

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "per-level-multiplier");

        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }
}
