package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Aversion extends EcoEnchant {
    public Aversion() {
        super(
                "aversion", EnchantmentType.NORMAL
        );
    }

    @EventHandler
    public void onEndermanTarget(@NotNull final EntityTargetLivingEntityEvent event) {
        if (!(event.getEntity() instanceof Enderman)) {
            return;
        }

        Enderman enderman = (Enderman) event.getEntity();
        LivingEntity target = event.getTarget();

        if (target == null) {
            return;
        }

        int level = EnchantChecks.getHelmetLevel(target, this);

        if (level == 0) {
            return;
        }

        if (this.getDisabledWorlds().contains(target.getWorld())) {
            return;
        }

        Bukkit.getLogger().info(event.getReason().name());
        //event.setCancelled(true);
    }
}
