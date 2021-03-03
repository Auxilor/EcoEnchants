package com.willfp.ecoenchants.enchantments.support.obtaining;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
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
    /**
     * Instance of ecoenchants.
     */
    private final EcoEnchantsPlugin plugin;

    /**
     * Create a new loot populator.
     *
     * @param plugin The plugin.
     */
    public LootPopulator(@NotNull final EcoEnchantsPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Populate a chunk's loot chests.
     *
     * @param world  The world to populate.
     * @param random Bukkit parity.
     * @param chunk  The chunk to populate.
     */
    public void populate(@NotNull final World world,
                         @NotNull final Random random,
                         @NotNull final Chunk chunk) {
        for (BlockState state : chunk.getTileEntities()) {
            Block block = state.getBlock();
            if (!(block.getState() instanceof Chest)) {
                continue;
            }

            Chest chestState = (Chest) block.getState();
            Inventory inventory = chestState.getBlockInventory();

            for (ItemStack item : inventory) {
                if (item == null) {
                    continue;
                }
                if (!EnchantmentTarget.ALL.getMaterials().contains(item.getType())) {
                    continue;
                }
                if (item.getType().equals(Material.BOOK)) {
                    continue;
                }

                HashMap<Enchantment, Integer> toAdd = new HashMap<>();

                ArrayList<EcoEnchant> enchantments = new ArrayList<>(EcoEnchants.values());
                Collections.shuffle(enchantments); // Prevent list bias towards early enchantments like telekinesis

                double multiplier = 0.01;
                if (item.getType().equals(Material.BOOK) || item.getType().equals(Material.ENCHANTED_BOOK)) {
                    multiplier /= plugin.getConfigYml().getInt("loot.book-times-less-likely");
                }

                if (plugin.getConfigYml().getBool("loot.reduce-probability.enabled")) {
                    multiplier /= plugin.getConfigYml().getDouble("loot.reduce-probability.factor");
                }

                for (EcoEnchant enchantment : enchantments) {
                    if (enchantment == null || enchantment.getRarity() == null) {
                        continue;
                    }

                    if (NumberUtils.randFloat(0, 1) > enchantment.getRarity().getLootProbability() * multiplier) {
                        continue;
                    }

                    if (!enchantment.isAvailableFromLoot()) {
                        continue;
                    }

                    if (!enchantment.canEnchantItem(item)) {
                        continue;
                    }

                    if (!enchantment.isEnabled()) {
                        continue;
                    }

                    AtomicBoolean anyConflicts = new AtomicBoolean(false);
                    toAdd.forEach((enchant, integer) -> {
                        if (enchantment.conflictsWithAny(toAdd.keySet())) {
                            anyConflicts.set(true);
                        }
                        if (enchant.conflictsWith(enchantment)) {
                            anyConflicts.set(true);
                        }

                        EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchant);
                        if (enchantment.getType().equals(ecoEnchant.getType()) && ecoEnchant.getType().isSingular()) {
                            anyConflicts.set(true);
                        }
                    });
                    if (anyConflicts.get()) {
                        continue;
                    }

                    int level;

                    if (enchantment.getType().equals(EnchantmentType.SPECIAL)) {
                        double enchantlevel1 = NumberUtils.randFloat(0, 1);
                        double enchantlevel2 = NumberUtils.bias(enchantlevel1, plugin.getConfigYml().getDouble("enchanting-table.special-bias"));
                        double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                        level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
                    } else {
                        double enchantlevel2 = NumberUtils.triangularDistribution(0, 1, 1);
                        double enchantlevel3 = 1 / (double) enchantment.getMaxLevel();
                        level = (int) Math.ceil(enchantlevel2 / enchantlevel3);
                    }

                    toAdd.put(enchantment, level);

                    if (plugin.getConfigYml().getBool("loot.reduce-probability.enabled")) {
                        multiplier /= plugin.getConfigYml().getDouble("loot.reduce-probability.factor");
                    }
                }

                if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                    toAdd.forEach(((enchantment, integer) -> meta.addStoredEnchant(enchantment, integer, false)));
                    item.setItemMeta(meta);
                } else {
                    ItemMeta meta = item.getItemMeta();
                    toAdd.forEach(((enchantment, integer) -> meta.addEnchant(enchantment, integer, false)));
                    item.setItemMeta(meta);
                }
            }
        }
    }
}
