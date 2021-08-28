package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Slicing extends EcoEnchant {
    private final ArrayList<LivingEntity> entities = new ArrayList<>();

    public Slicing() {
        super(
                "slicing", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onPlayerCollide(@NotNull final PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (!player.isGliding()) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!EnchantChecks.chestplate(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        for (Entity entity : player.getNearbyEntities(1, 1, 1)) {
            LivingEntity victim;
            if (entity instanceof LivingEntity) {
                victim = (LivingEntity) entity;
            } else {
                continue;
            }

            if (entities.contains(victim)) {
                continue;
            }

            double damage = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level");
            int level = EnchantChecks.getChestplateLevel(player, this);
            victim.damage(level * damage, player);
            entities.add(victim);

            Bukkit.getServer().getScheduler().runTaskLater(this.getPlugin(), () -> entities.remove(victim), this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "cooldown"));
            if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "damage-elytra")) {
                DurabilityUtils.damageItem(player, player.getInventory().getChestplate(), 1, 38);
            }
        }
    }
}
