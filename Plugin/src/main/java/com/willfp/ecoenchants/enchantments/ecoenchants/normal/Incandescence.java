package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Incandescence extends EcoEnchant {
    public Incandescence() {
        super(
                new EcoEnchantBuilder("incandescence", EnchantmentType.NORMAL, Target.Applicable.ARMOR, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onIncandescenceHurt(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!(event.getDamager() instanceof LivingEntity))
            return;

        Player player = (Player) event.getEntity();
        LivingEntity victim = (LivingEntity) event.getDamager();

        int totalIncandescencePoints = HasEnchant.getArmorPoints(player, this, true);

        if (totalIncandescencePoints == 0)
            return;

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            victim.setFireTicks(totalIncandescencePoints * this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-point") + this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-ticks"));
        }, 1);
    }
}
