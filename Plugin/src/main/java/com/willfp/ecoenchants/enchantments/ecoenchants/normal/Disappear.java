package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class Disappear extends EcoEnchant {
    public Disappear() {
        super(
                new EcoEnchantBuilder("disappear", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHurt(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            Player player = (Player) event.getEntity();

            if(player.getHealth() > EcoEnchants.DISAPPEAR.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "threshold"))
                return;

            final int points = EnchantChecks.getArmorPoints(player, this, 1);

            if (points == 0)
                return;

            int ticksPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level");
            final int ticks = ticksPerLevel * points;
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, ticks, 1, false, false, true));
        }, 1);
    }
}
