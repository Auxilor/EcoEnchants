package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Stab extends EcoEnchant {
    public Stab() {
        super(
                new EcoEnchantBuilder("stab", EnchantmentType.NORMAL, Target.Applicable.TRIDENT, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void stabHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player))
            return;

        if(!(event.getEntity() instanceof LivingEntity))
            return;

        if(event.isCancelled())
            return;

        Player player = (Player) event.getDamager();

        if(!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        double baseDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-base");
        double perLevelDamage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
        double damage = baseDamage + (level * perLevelDamage);

        event.setDamage(event.getDamage() + damage);
    }
}
