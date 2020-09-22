package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.events.armorequip.ArmorEquipEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;
public class Streamlining extends EcoEnchant {
    public Streamlining() {
        super(
                new EcoEnchantBuilder("streamlining", EnchantmentType.SPECIAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onEquip(ArmorEquipEvent event) {
        final Player player = event.getPlayer();

        new BukkitRunnable() {
            public void run() {
                if (!EnchantChecks.boots(player, EcoEnchants.STREAMLINING)) {
                    player.setWalkSpeed(0.2f);
                    return;
                }

                int level = EnchantChecks.getBootsLevel(player, EcoEnchants.STREAMLINING);
                double speed;
                player.setWalkSpeed((float) (0.2 + (level * EcoEnchants.STREAMLINING.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "speed-per-level"))));

            }
        }.runTaskLater(EcoEnchantsPlugin.getInstance(), 1);
    }

}
