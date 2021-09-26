package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.enchantments.util.WeakMetadata;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Necrotic extends EcoEnchant {
    public Necrotic() {
        super(
                "necrotic", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @EventHandler
    public void necroticKill(@NotNull final EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }

        if (!(event.getEntity() instanceof WitherSkeleton victim)) {
            return;
        }

        Player player = event.getEntity().getKiller();

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (WeakMetadata.SUMMONED_ENTITY_MEMORY.containsKey(event.getEntity())) {
            return;
        }

        ItemStack item = new ItemStack(Material.WITHER_SKELETON_SKULL, 1);

        new DropQueue(player)
                .addItem(item)
                .setLocation(victim.getLocation())
                .addXP(event.getDroppedExp())
                .push();
    }
}
