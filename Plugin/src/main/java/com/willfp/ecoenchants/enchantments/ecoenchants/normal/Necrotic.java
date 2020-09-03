package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.queue.DropQueue;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
public class Necrotic extends EcoEnchant {
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

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        if (NumberUtils.randFloat(0, 1) > level * 0.01 * chance)
            return;

        ItemStack item = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);

        new DropQueue(player)
                .addItem(item)
                .setLocation(victim.getLocation())
                .addXP(event.getDroppedExp())
                .push();
    }
}
