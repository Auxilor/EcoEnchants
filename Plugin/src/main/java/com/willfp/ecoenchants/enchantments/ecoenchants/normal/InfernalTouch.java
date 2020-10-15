package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.queue.DropQueue;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
public final class InfernalTouch extends EcoEnchant {
    public InfernalTouch() {
        super(
                new EcoEnchantBuilder("infernal_touch", EnchantmentType.NORMAL)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void infernalTouchBreak(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!EnchantChecks.mainhand(player, this)) return;

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if (event.getBlock().getState() instanceof Container)
            return;

        if (event.isCancelled())
            return;

        if (!AntigriefManager.canBreakBlock(player, block)) return;


        Collection<ItemStack> drops = new ArrayList<>();
        event.getItems().forEach((item -> {
            drops.add(item.getItemStack());
        }));

        List<ItemStack> newDrops = new ArrayList<ItemStack>();
        int experience = 0;

        for (ItemStack drop : drops) {
            if (!this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "smelt-cobblestone")) {
                if (drop.getType().equals(Material.COBBLESTONE)) {
                    newDrops.add(drop);
                    continue;
                }
            }

            Iterator<Recipe> iterator = Bukkit.recipeIterator();
            boolean couldSmelt = false;
            Recipe recipe = null;
            while (iterator.hasNext()) {
                recipe = iterator.next();
                if (!(recipe instanceof FurnaceRecipe)) {
                    continue;
                }
                if (((FurnaceRecipe) recipe).getInput().getType() == drop.getType()) {
                    couldSmelt = true;
                    break;
                }
            }
            if (couldSmelt) {
                drop.setType(recipe.getResult().getType());
                experience += (int) ((FurnaceRecipe) recipe).getExperience();

                if(drop.getType().equals(Material.IRON_INGOT)) {
                    experience += 1;
                    if(EnchantChecks.mainhand(player, Enchantment.LOOT_BONUS_BLOCKS)) {
                        int level = EnchantChecks.getMainhandLevel(player, LOOT_BONUS_BLOCKS);
                        drop.setAmount((int) Math.ceil(1/((double) level + 2) + ((double) level + 1)/2));
                    }
                } else if(drop.getType().equals(Material.GOLD_INGOT)) {
                    if(EnchantChecks.mainhand(player, Enchantment.LOOT_BONUS_BLOCKS)) {
                        int level = EnchantChecks.getMainhandLevel(player, LOOT_BONUS_BLOCKS);
                        drop.setAmount((int) Math.ceil(1/((double) level + 2) + ((double) level + 1)/2));
                    }
                }
            }
            newDrops.add(drop);
        }

        event.getItems().clear();

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(newDrops)
                .addXP(experience)
                .push();
    }
}
