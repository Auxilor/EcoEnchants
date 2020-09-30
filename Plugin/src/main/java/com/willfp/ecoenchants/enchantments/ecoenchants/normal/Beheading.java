package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.queue.DropQueue;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Beheading extends EcoEnchant {
    public Beheading() {
        super(
                new EcoEnchantBuilder("beheading", EnchantmentType.NORMAL,5.0)
        );
    }
    // START OF LISTENERS

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null)
            return;

        Player player = event.getEntity().getKiller();

        LivingEntity victim = event.getEntity();

        if (!EnchantChecks.mainhand(player, this)) return;

        int level = EnchantChecks.getMainhandLevel(player, this);

        if(!EnchantmentUtils.passedChance(this, level))
            return;

        ItemStack item;

        if(victim instanceof Player) {
            item = new ItemStack(Material.PLAYER_HEAD, 1);
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            assert meta != null;
            meta.setOwningPlayer((Player) victim);
            item.setItemMeta(meta);
        } else {
            if(event.getEntityType().equals(EntityType.ZOMBIE)) {
                item = new ItemStack(Material.ZOMBIE_HEAD, 1);
            }
            else if(event.getEntityType().equals(EntityType.SKELETON)) {
                item = new ItemStack(Material.SKELETON_SKULL, 1);
            }
            else if(event.getEntityType().equals(EntityType.CREEPER)) {
                item = new ItemStack(Material.CREEPER_HEAD, 1);
            }
            else if(event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
                item = new ItemStack(Material.DRAGON_HEAD, 1);
            }
            else return;
        }

        new DropQueue(player)
                .addItem(item)
                .addXP(event.getDroppedExp())
                .setLocation(victim.getLocation())
                .push();

        event.setDroppedExp(0);
    }
}
