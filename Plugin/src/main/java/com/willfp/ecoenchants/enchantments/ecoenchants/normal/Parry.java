package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Parry extends EcoEnchant {
    public Parry() {
        super(
                "parry", EnchantmentType.NORMAL
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void parryHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (event.isCancelled())
            return;

        Player player = (Player) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        if (this.getDisabledWorlds().contains(player.getWorld())) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");
        double bonus = 1 - (multiplier * level);
        event.setDamage(damage * bonus);
    }
}
