package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.events.armorequip.ArmorEquipEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
public class Thrive extends EcoEnchant {
    public Thrive() {
        super(
                new EcoEnchantBuilder("thrive", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        final Player player = event.getPlayer();

        new BukkitRunnable() {
            public void run() {
                int totalProsperityPoints = EnchantChecks.getArmorPoints(player, EcoEnchants.PROSPERITY, 0);
                int totalThrivePoints = EnchantChecks.getArmorPoints(player, EcoEnchants.THRIVE, 0);
                if (totalThrivePoints == 0 && totalProsperityPoints == 0) {
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                    return;
                }

                double thriveBonus = totalThrivePoints * EcoEnchants.THRIVE.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-point");
                double prosperityBonus = totalProsperityPoints * EcoEnchants.PROSPERITY.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-point");
                double bonus = thriveBonus + prosperityBonus;

                boolean onMaxHealth = false;
                if (player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                    onMaxHealth = true;

                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue() + bonus);
                boolean finalOnMaxHealth = onMaxHealth;
                new BukkitRunnable() {
                    public void run() {
                        if (finalOnMaxHealth) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 255, false, false, false));
                        }
                    }
                }.runTaskLater(EcoEnchantsPlugin.getInstance(), 1);
            }
        }.runTaskLater(EcoEnchantsPlugin.getInstance(), 1);
    }
}
