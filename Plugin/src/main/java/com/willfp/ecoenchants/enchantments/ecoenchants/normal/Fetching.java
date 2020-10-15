package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.events.entitydeathbyentity.EntityDeathByEntityEvent;
import com.willfp.ecoenchants.queue.DropQueue;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public final class Fetching extends EcoEnchant {
    public Fetching() {
        super(
                new EcoEnchantBuilder("fetching", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS

    @EventHandler(priority = EventPriority.HIGH)
    public void onWolfKill(EntityDeathByEntityEvent event) {
        LivingEntity entity = event.getVictim();

        if(entity instanceof Player && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "not-on-players"))
            return;

        if(!(event.getKiller() instanceof Wolf))
            return;

        Wolf wolf = (Wolf) event.getKiller();

        if(!wolf.isTamed() || wolf.getOwner() == null)
            return;

        if(!(wolf.getOwner() instanceof Player))
            return;

        Player player = (Player) wolf.getOwner();

        if(!(EnchantChecks.helmet(player, this)))
            return;

        int xp = event.getDroppedExp();
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
