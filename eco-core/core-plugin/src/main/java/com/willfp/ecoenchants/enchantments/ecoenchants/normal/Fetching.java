package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.eco.core.events.EntityDeathByEntityEvent;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Fetching extends EcoEnchant {
    public Fetching() {
        super(
                "fetching", EnchantmentType.NORMAL
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWolfKill(@NotNull final EntityDeathByEntityEvent event) {
        LivingEntity entity = event.getVictim();

        if (entity instanceof Player && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "not-on-players")) {
            return;
        }

        if (!(event.getKiller() instanceof Wolf wolf)) {
            return;
        }

        if (!wolf.isTamed() || wolf.getOwner() == null) {
            return;
        }

        if (!(wolf.getOwner() instanceof Player player)) {
            return;
        }

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!(EnchantChecks.helmet(player, this))) {
            return;
        }

        int xp = event.getXp();
        Collection<ItemStack> drops = event.getDrops();

        new DropQueue(player)
                .addItems(drops)
                .setLocation(entity.getLocation())
                .addXP(xp)
                .forceTelekinesis()
                .push();

        event.getDeathEvent().setDroppedExp(0);
        event.getDeathEvent().getDrops().clear();
    }
}
