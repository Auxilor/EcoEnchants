package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class Freerunner extends EcoEnchant {
    public Freerunner() {
        super(
                new EcoEnchantBuilder("freerunner", EnchantmentType.NORMAL, Target.Applicable.BOOTS, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        if(!event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            return;

        Player player = (Player) event.getEntity();

        if(!HasEnchant.playerBoots(player, this)) return;
        int level = HasEnchant.getPlayerBootsLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");

        if(Rand.randFloat(0, 1) > level * 0.01 * chance)
            return;

        event.setCancelled(true);
    }
}
