package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.queue.DropQueue;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
public final class Necrotic extends EcoEnchant {
    public Necrotic() {
        super(
                new EcoEnchantBuilder("necrotic", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void necroticKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;
        if (!(event.getEntity() instanceof WitherSkeleton))
            return;

        Player player = event.getEntity().getKiller();
        WitherSkeleton victim = (WitherSkeleton) event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        ItemStack item = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);

        new DropQueue(player)
                .addItem(item)
                .setLocation(victim.getLocation())
                .addXP(event.getDroppedExp())
                .push();
    }
}
