package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemConversions extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Instantiate item conversions.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public ItemConversions(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * On player hold item.
     * <p>
     * Listener for lore conversion.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void loreConverter(@NotNull final PlayerItemHeldEvent event) {
        if (this.getPlugin().getDisplayModule() == null) { // If plugin hasn't finished loading.
            return;
        }

        if (!((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().isUsingLoreGetter()) {
            return;
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        convertLore(itemStack);
    }

    /**
     * On player open inventory.
     * <p>
     * Listener for lore conversion.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void aggressiveLoreConverter(@NotNull final InventoryOpenEvent event) {
        if (this.getPlugin().getDisplayModule() == null) { // If plugin hasn't finished loading.
            return;
        }

        if (!((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().isUsingAggressiveLoreGetter()) {
            return;
        }

        Inventory inventory = event.getInventory();

        if (inventory.getHolder() == null) {
            return;
        }

        if (!(inventory.getHolder() instanceof BlockInventoryHolder)) {
            return;
        }

        for (ItemStack itemStack : inventory.getContents()) {
            convertLore(itemStack);
        }
    }

    private void convertLore(@Nullable final ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        Map<Enchantment, Integer> toAdd = new HashMap<>();

        List<String> lore = meta.getLore();

        if (lore == null) {
            return;
        }

        for (String line : new ArrayList<>(lore)) {
            String rawLine = line;
            line = ChatColor.stripColor(line);

            EcoEnchant enchant;
            int level;
            List<String> lineSplit = new ArrayList<>(Arrays.asList(line.split(" ")));
            if (lineSplit.size() == 0) {
                continue;
            }
            if (lineSplit.size() == 1) {
                enchant = EcoEnchants.getByName(lineSplit.get(0));
                level = 1;
            } else {
                EcoEnchant attemptFullLine = EcoEnchants.getByName(line);

                if (attemptFullLine != null) {
                    enchant = attemptFullLine;
                    level = 1;
                } else {
                    String levelString = lineSplit.get(lineSplit.size() - 1);
                    lineSplit.remove(levelString);
                    levelString = levelString.trim();

                    try {
                        level = NumberUtils.fromNumeral(levelString);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }

                    String enchantName = String.join(" ", lineSplit);
                    enchant = EcoEnchants.getByName(enchantName);
                }
            }

            if (enchant != null) {
                lore.remove(rawLine);
                toAdd.put(enchant, level);
            }
        }

        if (meta instanceof EnchantmentStorageMeta) {
            lore.clear();
            toAdd.forEach((enchantment, integer) -> ((EnchantmentStorageMeta) meta).addStoredEnchant(enchantment, integer, true));
        } else {
            toAdd.forEach((enchantment, integer) -> meta.addEnchant(enchantment, integer, true));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
    }

    /**
     * On player hold item.
     * <p>
     * Listener for hide fixer.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void hideFixer(@NotNull final PlayerItemHeldEvent event) {
        if (this.getPlugin().getDisplayModule() == null) { // If plugin hasn't finished loading.
            return;
        }

        if (!((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().isUsingExperimentalHideFixer()) {
            return;
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        hideFixItem(itemStack);
    }

    /**
     * On player open inventory.
     * <p>
     * Listener for hide fixer.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void aggressiveHideFixer(@NotNull final InventoryOpenEvent event) {
        if (this.getPlugin().getDisplayModule() == null) { // If plugin hasn't finished loading.
            return;
        }

        if (!((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().isUsingAggressiveExperimentalHideFixer()) {
            return;
        }

        Inventory inventory = event.getInventory();

        if (inventory.getHolder() == null) {
            return;
        }

        if (!(inventory.getHolder() instanceof BlockInventoryHolder)) {
            return;
        }

        for (ItemStack itemStack : inventory.getContents()) {
            hideFixItem(itemStack);
        }
    }

    private void hideFixItem(@Nullable final ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        if (!EnchantmentTarget.ALL.getMaterials().contains(itemStack.getType())) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        if (((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().isUsingForceHideFixer()) {
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        } else {
            if (meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS) && meta.hasItemFlag(ItemFlag.HIDE_POTION_EFFECTS)) {
                meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS);
            }
        }

        itemStack.setItemMeta(meta);
    }

    /**
     * On player hold item.
     * <p>
     * Listener for hide fixer.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void levelClamp(@NotNull final PlayerItemHeldEvent event) {
        if (!this.getPlugin().getConfigYml().getBool("advanced.level-clamp.enabled")) {
            return;
        }

        if (event.getPlayer().hasPermission("ecoenchants.bypasslevelclamp")) {
            return;
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        clampItemLevels(itemStack);
    }

    private void clampItemLevels(@Nullable final ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        if (!EnchantmentTarget.ALL.getMaterials().contains(itemStack.getType())) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        if (meta instanceof EnchantmentStorageMeta) {
            new HashMap<>(((EnchantmentStorageMeta) meta).getStoredEnchants()).forEach((enchantment, integer) -> {
                if (integer > enchantment.getMaxLevel()) {
                    ((EnchantmentStorageMeta) meta).removeStoredEnchant(enchantment);
                    ((EnchantmentStorageMeta) meta).addStoredEnchant(enchantment, enchantment.getMaxLevel(), true);
                }
            });
        } else {
            new HashMap<>(meta.getEnchants()).forEach((enchantment, integer) -> {
                if (integer > enchantment.getMaxLevel()) {
                    meta.removeEnchant(enchantment);
                    meta.addEnchant(enchantment, enchantment.getMaxLevel(), true);
                }
            });
        }

        itemStack.setItemMeta(meta);
    }
}
