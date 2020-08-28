package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.integrations.antigrief.plugins.AntigriefGriefPrevention;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@SuppressWarnings("deprecation")
public class Deflection extends EcoEnchant {
    public Deflection() {
        super(
                new EcoEnchantBuilder("deflection", EnchantmentType.NORMAL, Target.Applicable.SHIELD, 4.0)
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
        if (!HasEnchant.playerOffhand(player, this) && !HasEnchant.playerHeld(player, this)) return;
        if(HasEnchant.playerOffhand(player, this)) level = HasEnchant.getPlayerOffhandLevel(player, this);
        else level = HasEnchant.getPlayerLevel(player, this);

        double perlevel = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "percent-deflected-per-level");
        double damagePercent = (perlevel/100) * level;
        double damage = event.getDamage() * damagePercent;

        victim.damage(damage, player);
    }
}
