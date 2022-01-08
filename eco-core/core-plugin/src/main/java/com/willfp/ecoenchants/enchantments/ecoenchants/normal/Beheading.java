package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.eco.core.items.builder.SkullBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class Beheading extends EcoEnchant {
    public Beheading() {
        super(
                "beheading", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @EventHandler
    public void onDeath(@NotNull final EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }

        Player player = event.getEntity().getKiller();

        if (!this.areRequirementsMet(player)) {
            return;
        }

        LivingEntity victim = event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        ItemStack item;

        if (victim instanceof Player) {
            item = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            assert meta != null;
            meta.setOwningPlayer((Player) victim);
            item.setItemMeta(meta);
        } else {
            item = getHead(event.getEntityType());
            if (item == null) {
                if (event.getEntityType().equals(EntityType.ZOMBIE)) {
                    item = new ItemStack(Material.ZOMBIE_HEAD, 1);
                } else if (event.getEntityType().equals(EntityType.SKELETON)) {
                    item = new ItemStack(Material.SKELETON_SKULL, 1);
                } else if (event.getEntityType().equals(EntityType.CREEPER)) {
                    item = new ItemStack(Material.CREEPER_HEAD, 1);
                } else if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
                    item = new ItemStack(Material.DRAGON_HEAD, 1);
                } else {
                    return;
                }
            }
        }

        new DropQueue(player)
                .addItem(item)
                .addXP(event.getDroppedExp())
                .setLocation(victim.getLocation())
                .push();

        event.setDroppedExp(0);
    }

    ItemStack getHead(@NotNull final EntityType type) {
        for (String s : this.getConfig().getStrings("custom-heads")) {
            String[] split = s.split("::");
            if (!type.name().equalsIgnoreCase(split[0])) {
                continue;
            }
            return new SkullBuilder().setSkullTexture(split[1]).build();
        }
        return null;
    }
}
