package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
public class Kinetic extends EcoEnchant {
    public Kinetic() {
        super(
                new EcoEnchantBuilder("kinetic", EnchantmentType.NORMAL, Target.Applicable.ELYTRA, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onKineticHurt(EntityDamageEvent event) {
        if(event.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) return;

        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        if(!HasEnchant.playerElytra(player, this)) return;

        int level = HasEnchant.getPlayerChestplateLevel(player, this);

        double reduction = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "reduction-per-level");
        double multiplier = 1 - ((reduction/100) * level);
        event.setDamage(event.getDamage() * multiplier);
    }
}
