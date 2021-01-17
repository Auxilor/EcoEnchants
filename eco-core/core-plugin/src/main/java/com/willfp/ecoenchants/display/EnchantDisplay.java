package com.willfp.ecoenchants.display;

import com.google.common.collect.Lists;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.config.updating.annotations.ConfigUpdater;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.display.options.DisplayOptions;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * All methods and fields pertaining to showing players the enchantments on their items.
 */
@UtilityClass
public class EnchantDisplay {
    /**
     * Instance of EcoEnchants.
     */
    private static final AbstractEcoPlugin PLUGIN = EcoEnchantsPlugin.getInstance();

    /**
     * The meta key to hide enchantments in lore.
     * <p>
     * EcoEnchants packet lore implementation of HideEnchants.
     */
    public static final NamespacedKey KEY_SKIP = PLUGIN.getNamespacedKeyFactory().create("ecoenchantlore-skip");

    /**
     * The meta key to notify the server that an item is from a villager trade.
     * <p>
     * Bit of a bodge - plan on making it better.
     */
    public static final NamespacedKey KEY_V = PLUGIN.getNamespacedKeyFactory().create("ecoenchantlore-v");

    /**
     * The prefix for all enchantment lines to have in lore.
     */
    public static final String PREFIX = "§w";

    /**
     * The configurable options for displaying enchantments.
     */
    public static final DisplayOptions OPTIONS = new DisplayOptions();

    /**
     * Update config values.
     */
    @ConfigUpdater
    public static void update() {
        OPTIONS.update();
        EnchantmentCache.update();
    }

    /**
     * Bodge to fix hidden enchantments from villagers.
     * <p>
     * It isn't recommended to mess with this unless you <b>really</b> know your way around EcoEnchants.
     *
     * @param item The item to modify.
     * @return The item, with KEY_V.
     */
    public static ItemStack addV(@Nullable final ItemStack item) {
        if (item == null || item.getItemMeta() == null) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(KEY_V, PersistentDataType.INTEGER, 1);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Revert display.
     *
     * @param item The item to revert.
     * @return The item, updated.
     */
    public static ItemStack revertDisplay(@Nullable final ItemStack item) {
        if (item == null || !EnchantmentTarget.ALL.getMaterials().contains(item.getType()) || item.getItemMeta() == null) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        List<String> itemLore;

        if (meta.hasLore()) {
            itemLore = meta.getLore();
        } else {
            itemLore = new ArrayList<>();
        }

        if (itemLore == null) {
            itemLore = new ArrayList<>();
        }

        if (meta.getPersistentDataContainer().has(KEY_V, PersistentDataType.INTEGER)) {
            meta.getPersistentDataContainer().remove(KEY_V);
        }
        itemLore.removeIf(s -> s.startsWith(PREFIX));

        if (!meta.getPersistentDataContainer().has(KEY_SKIP, PersistentDataType.INTEGER)) {
            if (meta instanceof EnchantmentStorageMeta) {
                meta.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS); // Thanks ShaneBee!
            }
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.getPersistentDataContainer().remove(KEY_SKIP);
        meta.setLore(itemLore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Show all enchantments in item lore.
     *
     * @param item The item to update.
     * @return The item, updated.
     */
    public static ItemStack displayEnchantments(@Nullable final ItemStack item) {
        return displayEnchantments(item, false);
    }

    /**
     * Show all enchantments in item lore.
     *
     * @param item         The item to update.
     * @param hideEnchants If enchantments should be hidden.
     * @return The item, updated.
     */
    public static ItemStack displayEnchantments(@Nullable final ItemStack item,
                                                final boolean hideEnchants) {
        boolean hide = hideEnchants;
        if (item == null || item.getItemMeta() == null || !EnchantmentTarget.ALL.getMaterials().contains(item.getType())) {
            return item;
        }

        if (item.getItemMeta().getPersistentDataContainer().has(KEY_V, PersistentDataType.INTEGER) && hideEnchants) {
            hide = false;
        }

        revertDisplay(item);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        List<String> itemLore = new ArrayList<>();

        if (hide || meta.getPersistentDataContainer().has(KEY_SKIP, PersistentDataType.INTEGER)) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            meta.getPersistentDataContainer().set(KEY_SKIP, PersistentDataType.INTEGER, 1);
            item.setItemMeta(meta);
            return item;
        }

        if (meta.hasLore()) {
            itemLore = meta.getLore();
        }

        if (itemLore == null) {
            itemLore = new ArrayList<>();
        }

        List<String> lore = new ArrayList<>();

        LinkedHashMap<Enchantment, Integer> enchantments = new LinkedHashMap<>();
        List<Enchantment> forRemoval = new ArrayList<>();

        if (meta instanceof EnchantmentStorageMeta) {
            enchantments.putAll(((EnchantmentStorageMeta) meta).getStoredEnchants());
        } else {
            enchantments.putAll(meta.getEnchants());
        }

        enchantments.entrySet().removeIf(enchantmentIntegerEntry -> enchantmentIntegerEntry.getValue().equals(0));

        List<Enchantment> unsorted = new ArrayList<>();
        enchantments.forEach((enchantment, integer) -> unsorted.add(enchantment));

        HashMap<Enchantment, Integer> tempEnchantments = new HashMap<>(enchantments);

        OPTIONS.getSorter().sortEnchantments(unsorted);

        enchantments.clear();
        unsorted.forEach(enchantment -> {
            enchantments.put(enchantment, tempEnchantments.get(enchantment));
        });

        enchantments.forEach((enchantment, level) -> {
            if (EcoEnchants.getFromEnchantment(enchantment) == null) {
                return;
            }

            EcoEnchant ecoEnchant = EcoEnchants.getFromEnchantment(enchantment);

            if (!ecoEnchant.isEnabled()) {
                forRemoval.add(enchantment);
            }
        });
        forRemoval.forEach(enchantment -> {
            enchantments.remove(enchantment);
            if (meta instanceof EnchantmentStorageMeta) {
                ((EnchantmentStorageMeta) meta).removeStoredEnchant(enchantment);
            } else {
                meta.removeEnchant(enchantment);
            }
        });

        enchantments.forEach((enchantment, level) -> {
            String name = EnchantmentCache.getEntry(enchantment).getName();

            if (!(enchantment.getMaxLevel() == 1 && level == 1)) {
                if (OPTIONS.getNumbersOptions().isUseNumerals() && item.getEnchantmentLevel(enchantment) < OPTIONS.getNumbersOptions().getThreshold()) {
                    name += " " + NumberUtils.toNumeral(level);
                } else {
                    name += " " + level;
                }
            }

            lore.add(PREFIX + name);
            if (enchantments.size() <= OPTIONS.getDescriptionOptions().getThreshold() && OPTIONS.getDescriptionOptions().isEnabled()) {
                lore.addAll(EnchantmentCache.getEntry(enchantment).getDescription());
            }
        });

        if (OPTIONS.getShrinkOptions().isEnabled() && (enchantments.size() > OPTIONS.getShrinkOptions().getThreshold())) {
            List<List<String>> partitionedCombinedLoreList = Lists.partition(lore, OPTIONS.getShrinkOptions().getShrinkPerLine());
            List<String> newLore = new ArrayList<>();
            partitionedCombinedLoreList.forEach(list -> {
                StringBuilder builder = new StringBuilder();
                for (String s : list) {
                    builder.append(s);
                    builder.append(", ");
                }
                String line = builder.toString();
                line = line.substring(0, line.length() - 2);
                newLore.add(line);
            });
            lore.clear();
            lore.addAll(newLore);
        }

        if (meta instanceof EnchantmentStorageMeta) {
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        lore.addAll(itemLore);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
