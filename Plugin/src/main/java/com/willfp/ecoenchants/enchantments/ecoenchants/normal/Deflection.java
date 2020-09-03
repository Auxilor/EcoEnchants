package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Deflection extends EcoEnchant {
    public Deflection() {
        super(
                new EcoEnchantBuilder("deflection", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onDeflect(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof LivingEntity))
            return;

        Player player = (Player) event.getEntity();

        LivingEntity victim = (LivingEntity) event.getDamager();

        if(!player.isBlocking()) return;

        if(!AntigriefManager.canInjure(player, victim)) return;

        int level;
        if (!EnchantChecks.offhand(player, this) && !EnchantChecks.mainhand(player, this)) return;
        if(EnchantChecks.offhand(player, this)) level = EnchantChecks.getOffhandLevel(player, this);
        else level = EnchantChecks.getMainhandLevel(player, this);

        double perlevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-deflected-per-level");
        double damagePercent = (perlevel/100) * level;
        double damage = event.getDamage() * damagePercent;

        victim.damage(damage, player);
    }
}
