package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.events.armorequip.ArmorEquipEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class Thrive extends EcoEnchant {
    public Thrive() {
        super(
                "thrive", EnchantmentType.NORMAL
        );
    }
    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
        final Player player = event.getPlayer();

        this.getPlugin().getScheduler().runLater(() -> {
            int totalProsperityPoints = EnchantChecks.getArmorPoints(player, EcoEnchants.PROSPERITY, 0);
            int totalThrivePoints = EnchantChecks.getArmorPoints(player, EcoEnchants.THRIVE, 0);
            if (totalThrivePoints == 0 && totalProsperityPoints == 0) {
                player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue());
                return;
            }
            if(EcoEnchants.THRIVE.getDisabledWorlds().contains(player.getWorld())) return;

            double thriveBonus = totalThrivePoints * EcoEnchants.THRIVE.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-point");
            double prosperityBonus = totalProsperityPoints * EcoEnchants.PROSPERITY.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "health-per-point");
            double bonus = thriveBonus + prosperityBonus;

            boolean onMaxHealth = false;
            if (player.getHealth() == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())
                onMaxHealth = true;

            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getDefaultValue() + bonus);
            boolean finalOnMaxHealth = onMaxHealth;
            this.getPlugin().getScheduler().runLater(() -> {
                if (finalOnMaxHealth) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 255, false, false, false));
                }
            }, 1);
        }, 1);
    }
}
