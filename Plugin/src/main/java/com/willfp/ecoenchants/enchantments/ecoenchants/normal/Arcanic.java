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

public class Arcanic extends EcoEnchant {
    public Arcanic() {
        super(
                new EcoEnchantBuilder("arcanic", EnchantmentType.NORMAL, Target.Applicable.ARMOR, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onEffect(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

        if(!(event.getCause().equals(EntityDamageEvent.DamageCause.POISON) || event.getCause().equals(EntityDamageEvent.DamageCause.WITHER)))
            return;

        Player player = (Player) event.getEntity();

        int totalArcanicPoints = HasEnchant.getArmorPoints(player, this, false);

        if(totalArcanicPoints == 0)
            return;

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-point");
        if(Rand.randFloat(0, 1) > totalArcanicPoints * 0.01 * chance)
            return;

        event.setCancelled(true);
    }
}
