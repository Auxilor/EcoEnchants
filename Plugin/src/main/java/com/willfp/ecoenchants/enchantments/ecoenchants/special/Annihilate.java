package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class Annihilate extends EcoEnchant {
    public Annihilate() {
        super(
                new EcoEnchantBuilder("annihilate", EnchantmentType.SPECIAL, new Target.Applicable[]{Target.Applicable.SWORD, Target.Applicable.AXE}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;

        if(event.isCancelled()) return;

        Player player = (Player) event.getDamager();

        LivingEntity victim = (LivingEntity) event.getEntity();

        if(victim.hasMetadata("NPC")) return;

        if(!AntigriefManager.canInjure(player, victim)) return;

        if (!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        double baseMultiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-multiplier");
        Vector vector = player.getLocation().toVector().clone().subtract(victim.getLocation().toVector()).normalize().multiply(level * baseMultiplier).multiply(-1);
        vector.setY(0.2);
        victim.setVelocity(vector);
    }
}
