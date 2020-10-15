package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.ItemDurability;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
public final class Slicing extends EcoEnchant {
    public Slicing() {
        super(
                new EcoEnchantBuilder("slicing", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS

    ArrayList<LivingEntity> entities = new ArrayList<LivingEntity>();

    @EventHandler
    public void onPlayerCollide(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.isGliding())
            return;

        if (!EnchantChecks.chestplate(player, this)) return;

        for (Entity entity : player.getNearbyEntities(1, 1, 1)) {
            LivingEntity victim;
            if (entity instanceof LivingEntity) {
                victim = (LivingEntity) entity;
            } else {
                continue;
            }

            if (entities.contains(victim))
                continue;

            double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
            int level = EnchantChecks.getMainhandLevel(player, this);
            victim.damage(level * damage, player);
            entities.add(victim);

            Bukkit.getServer().getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), new Runnable() {
                public void run() {
                    entities.remove(victim);
                }
            }, this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "cooldown"));
            if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "damage-elytra")) {
                ItemDurability.damageItem(player, player.getInventory().getChestplate(), 1, 38);
            }
        }
    }
}
