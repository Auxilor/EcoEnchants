package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Lightning;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Zeus extends EcoEnchant {
    public Zeus() {
        super(
                new EcoEnchantBuilder("zeus", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.BOW, Target.Applicable.CROSSBOW}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void zeusHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Arrow))
            return;
        if(!(((Arrow) event.getDamager()).getShooter() instanceof Player))
            return;
        if(!(event.getEntity() instanceof LivingEntity))
            return;

        Player player = (Player) ((Arrow) event.getDamager()).getShooter();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(!AntigriefManager.canInjure(player, victim)) return;

        if(!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        if(Rand.randFloat(0, 1) > level * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level"))
            return;

        double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "lightning-damage");

        Lightning.strike(victim, damage);
    }
}
