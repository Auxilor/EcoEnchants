package com.willfp.ecoenchants.effects;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.events.armorequip.ArmorEquipEvent;
import com.willfp.ecoenchants.util.optional.Prerequisite;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class EffectsEnchantment extends EcoEnchant {
    protected EffectsEnchantment(String key, EnchantmentType type, Prerequisite... prerequisites) {
        super(key, type, EffectsMain.class, prerequisites);
    }

    public abstract PotionEffectType getPotionEffect();

    @EventHandler
    public void onEquip(ArmorEquipEvent event) {
        final Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            if (player.hasPotionEffect(this.getPotionEffect())) {
                if (player.getPotionEffect(this.getPotionEffect()).getDuration() >= 1639) {
                    player.removePotionEffect(this.getPotionEffect());
                }
            }

            int level = EnchantChecks.getArmorPoints(player, this);
            if(this.getDisabledWorlds().contains(player.getWorld())) return;
            if(level > 0) {
                player.addPotionEffect(new PotionEffect(this.getPotionEffect(), 0x6fffffff, level - 1, false, false, true));
            }
        }, 1);
    }
}
