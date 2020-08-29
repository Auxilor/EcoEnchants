package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boss;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BossHunter extends EcoEnchant {
    public BossHunter() {
        super(
                new EcoEnchantBuilder("boss_hunter", EnchantmentType.NORMAL, new Target.Applicable[]{Target.Applicable.BOW, Target.Applicable.CROSSBOW}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Arrow)) return;

        if(!(((Arrow) event.getDamager()).getShooter() instanceof Player)) return;

        if(!(event.getEntity() instanceof Boss || event.getEntity() instanceof ElderGuardian)) return;

        Player player = (Player) ((Arrow) event.getDamager()).getShooter();
        Arrow arrow = (Arrow) event.getDamager();

        if(!HasEnchant.playerHeld(player, this)) return;
        int level = HasEnchant.getPlayerLevel(player, this);

        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier");

        double damageMultiplier = (level * multiplier) + 1;

        event.setDamage(event.getDamage() * damageMultiplier);
    }
}
