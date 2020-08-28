package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@SuppressWarnings("deprecation")
public class HarmlessnessCurse extends EcoEnchant {
    public HarmlessnessCurse() {
        super(
                new EcoEnchantBuilder("harmlessness_curse", EnchantmentType.CURSE, new Target.Applicable[]{Target.Applicable.SWORD, Target.Applicable.AXE}, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void harmlessnessHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        if (event.getEntity() instanceof Monster)
            return;

        Player player = (Player) event.getDamager();

        if (!HasEnchant.playerHeld(player, this)) return;

        if (Rand.randFloat(0, 1) > 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance"))
            return;

        event.setDamage(0);
        event.setCancelled(true);
    }
}
