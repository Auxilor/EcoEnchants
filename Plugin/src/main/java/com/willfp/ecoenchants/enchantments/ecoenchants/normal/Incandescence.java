package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class Incandescence extends EcoEnchant {
    public Incandescence() {
        super(
                new EcoEnchantBuilder("incandescence", EnchantmentType.NORMAL, 5.0)
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

        int totalIncandescencePoints = EnchantChecks.getArmorPoints(player, this, 1);

        if (totalIncandescencePoints == 0)
            return;

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            victim.setFireTicks(totalIncandescencePoints * this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-point") + this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-ticks"));
        }, 1);
    }
}
