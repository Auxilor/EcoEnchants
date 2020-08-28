package com.willfp.ecoenchants.naturalloot;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.lore.EnchantLore;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.Bias;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class LootPopulator extends BlockPopulator {
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
        if(!ConfigManager.getConfig().getBool("loot.enabled"))
            return;

        for(BlockState state : chunk.getTileEntities()) {
            Block block = state.getBlock();
            if(!(block.getState() instanceof Chest)) continue;

            Chest chestState = (Chest) block.getState();
            Inventory inventory = chestState.getBlockInventory();

            for(ItemStack item : inventory) {
                if(item == null) continue;
                if(!Target.Applicable.ALL.getMaterials().contains(item.getType())) continue;
                if(item.getType().equals(Material.BOOK)) continue;

                HashMap<Enchantment, Integer> toAdd = new HashMap<>();

                ArrayList<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.getAll());
                Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

                double multiplier = 0.01;
                if (Target.Applicable.BOOK.getMaterials().contains(item.getType())) {
                    multiplier /= ConfigManager.getConfig().getInt("loot.book-times-less-likely");
                }

                if (ConfigManager.getConfig().getBool("loot.reduce-probability.enabled")) {
                    multiplier /= ConfigManager.getConfig().getDouble("loot.reduce-probability.factor");
                }

                for (EcoEnchant enchantment : enchantments) {
                    if(enchantment == null || enchantment.getRarity() == null) continue;

                    if (Rand.randFloat(0, 1) > enchantment.getRarity().getLootProbability() * multiplier)
                        continue;
                    if (!enchantment.canGetFromLoot())
                        continue;
                    if(!enchantment.canEnchantItem(item))
                        continue;

                    AtomicBoolean anyConflicts = new AtomicBoolean(false);
                    toAdd.forEach((enchant, integer) -> {
                        if (enchantment.conflictsWithAny(toAdd.keySet())) anyConflicts.set(true);
                        if (enchant.conflictsWith(enchantment)) anyConflicts.set(true);

                        EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchant);
                        if (enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL) && ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) anyConflicts.set(true);
                        if (enchantment.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT) && ecoEnchant.getType().equals(EcoEnchant.EnchantmentType.ARTIFACT)) anyConflicts.set(true);
                    });
                    if (anyConflicts.get()) continue;

                    int level;

                    if(enchantment.getType().equals(EcoEnchant.EnchantmentType.SPECIAL)) {
                        double enchantlevel1 = Rand.randFloat(0, 1);
                        double enchantlevel2 = Bias.bias(enchantlevel1, ConfigManager.getConfig().getDouble("enchanting-table.special-bias"));
                        double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                        level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
                    } else {
                        double enchantlevel2 = Rand.triangularDistribution(0, 1, 1);
                        double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                        level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
                    }

                    toAdd.put(enchantment, level);

                    if (ConfigManager.getConfig().getBool("loot.reduce-probability.enabled")) {
                        multiplier /= ConfigManager.getConfig().getDouble("loot.reduce-probability.factor");
                    }
                }

                if(item.getItemMeta() instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    toAdd.forEach(((enchantment, integer) -> {
                        meta.addStoredEnchant(enchantment, integer, false);
                    }));
                    item.setItemMeta(meta);
                } else {
                    ItemMeta meta = item.getItemMeta();
                    toAdd.forEach(((enchantment, integer) -> {
                        meta.addEnchant(enchantment, integer, false);
                    }));
                    item.setItemMeta(meta);
                }
            }
        }
    }
}
