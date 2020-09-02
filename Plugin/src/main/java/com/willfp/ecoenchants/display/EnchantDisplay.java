package com.willfp.ecoenchants.display;

import com.google.common.collect.Lists;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.NumberUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * All methods and fields pertaining to showing players the enchantments on their items.
 */
public class EnchantDisplay {

    /**
     * The meta key of the length of enchantments in lore
     */
    private static final NamespacedKey key = new NamespacedKey(EcoEnchantsPlugin.getInstance(), "ecoenchantlore-len");

    /**
     * The meta key to hide enchantments in lore
     */
    public static final NamespacedKey keySkip = new NamespacedKey(EcoEnchantsPlugin.getInstance(), "ecoenchantlore-skip");


    private static String normalColor;
    private static String curseColor;
    private static String specialColor;
    private static String artifactColor;
    private static String descriptionColor;

    private static int numbersThreshold;
    private static boolean useNumerals;

    private static int describeThreshold;
    private static boolean useDescribe;

    private static int shrinkThreshold;
    private static int shrinkPerLine;
    private static boolean useShrink;

    /**
     * Update config values
     */
    public static void update() {
        descriptionColor = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("description-color"));
        curseColor = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("curse-color"));
        specialColor = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("special-color"));
        artifactColor = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("artifact-color"));
        normalColor = ChatColor.translateAlternateColorCodes('&', ConfigManager.getLang().getString("not-curse-color"));

        useNumerals = ConfigManager.getConfig().getBool("lore.use-numerals");
        numbersThreshold = ConfigManager.getConfig().getInt("lore.use-numbers-above-threshold");

        describeThreshold = ConfigManager.getConfig().getInt("lore.describe.before-lines");
        useDescribe = ConfigManager.getConfig().getBool("lore.describe.enabled");

        shrinkThreshold = ConfigManager.getConfig().getInt("lore.shrink.after-lines");
        useShrink = ConfigManager.getConfig().getBool("lore.shrink.enabled");
        shrinkPerLine = ConfigManager.getConfig().getInt("lore.shrink.maximum-per-line");
    }

    /**
     * Revert display
     * @param item The item to revert
     * @return The item, updated
     */
    public static ItemStack revertDisplay(ItemStack item) {
        if(item == null) return null;

        if(!Target.Applicable.ALL.getMaterials().contains(item.getType()))
            return item;

        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();

        if(meta == null) return item;

        if(meta.hasLore())
            itemLore = meta.getLore();

        try {
            if (meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                int enchantLoreLength = meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
                if(itemLore.size() >= enchantLoreLength) {
                    itemLore.subList(0, enchantLoreLength).clear();
                }
            }
        } catch (NullPointerException ignored) {}



        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta metaBook = (EnchantmentStorageMeta) meta;
            metaBook.removeItemFlags(ItemFlag.HIDE_POTION_EFFECTS); // Thanks ShaneBee!
            metaBook.removeItemFlags(ItemFlag.HIDE_ENCHANTS); // Here just in case
            metaBook.setLore(itemLore);
            item.setItemMeta(metaBook);
        } else {
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(itemLore);
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Show all enchantments in item lore
     * @param item The item to update
     * @return The item, updated
     */
    public static ItemStack displayEnchantments(ItemStack item) {
        if(item == null) return null;

        ItemStack oldItem = item.clone();

        if(!Target.Applicable.ALL.getMaterials().contains(item.getType()))
            return oldItem;

        item = revertDisplay(item);

        ItemMeta meta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();

        int loreStart = 0;

        if(meta == null) return oldItem;

        if(meta.getPersistentDataContainer().has(keySkip, PersistentDataType.INTEGER))
            return oldItem;

        if(meta.hasLore())
            itemLore = meta.getLore();

        List<String> normalLore = new ArrayList<>();
        List<String> curseLore = new ArrayList<>();

        List<String> enchantLore = new ArrayList<>();

        Map<Enchantment, Integer> enchantments;
        List<Enchantment> forRemoval = new ArrayList<>();

        if(meta instanceof EnchantmentStorageMeta) {
            enchantments = ((EnchantmentStorageMeta) meta).getStoredEnchants();
        } else {
            enchantments = meta.getEnchants();
        }

        final ItemStack finalItem = item;
        enchantments.forEach(((enchantment, integer) -> {
            boolean isEcoEnchant = EcoEnchants.getFromEnchantment(enchantment) != null;

            String name;
            String color;
            List<String> description;

            EcoEnchant.EnchantmentType type;

            if(enchantment.isCursed()) type = EcoEnchant.EnchantmentType.CURSE;
            else type = EcoEnchant.EnchantmentType.NORMAL;

            if(isEcoEnchant) {
                type = EcoEnchants.getFromEnchantment(enchantment).getType();
            }

            boolean isMaxLevelOne = false;
            if(enchantment.getMaxLevel() == 1 && integer == 1) isMaxLevelOne = true;

            switch(type) {
                case ARTIFACT:
                    color = artifactColor;
                    break;
                case SPECIAL:
                    color = specialColor;
                    break;
                case CURSE:
                    color = curseColor;
                    break;
                default:
                    color = normalColor;
                    break;
            }

            if(isEcoEnchant) {
                name = enchantment.getName();
                description = EcoEnchants.getFromEnchantment(enchantment).getDescription();
                description.replaceAll(line -> descriptionColor + line);
                if(EcoEnchants.getFromEnchantment(enchantment).isDisabled()) forRemoval.add(enchantment);
            } else {
                name = ConfigManager.getLang().getString("vanilla." + enchantment.getKey().getKey() + ".name");
                description = Arrays.asList(WordUtils.wrap(ConfigManager.getLang().getString("vanilla." + enchantment.getKey().getKey() + ".description"), ConfigManager.getConfig().getInt("lore.describe.wrap"), "\n", false).split("\\r?\\n"));
                description.replaceAll(line -> descriptionColor + line);
            }

            if(!(isMaxLevelOne || type == EcoEnchant.EnchantmentType.CURSE)) {
                if (useNumerals && finalItem.getEnchantmentLevel(enchantment) < numbersThreshold) {
                    name += " " + NumberUtils.toNumeral(integer);
                } else {
                    name += " " + integer;
                }
            }

            boolean describe = false;
            if(enchantments.size() <= describeThreshold && useDescribe) {
                describe = true;
            }

            if(type == EcoEnchant.EnchantmentType.CURSE) {
                curseLore.add(color + name);
                if(describe) curseLore.addAll(description);
            }
            else {
                normalLore.add(color + name);
                if(describe) normalLore.addAll(description);
            }
        }));

        List<String> combinedLore = new ArrayList<>();
        combinedLore.addAll(normalLore);
        combinedLore.addAll(curseLore);

        if (useShrink && (enchantments.size() > shrinkThreshold)) {
            List<List<String>> partitionedCombinedLoreList = Lists.partition(combinedLore, shrinkPerLine);
            partitionedCombinedLoreList.forEach((list) -> {
                StringBuilder builder = new StringBuilder();
                for (String s : list) {
                    builder.append(s);
                    builder.append(", ");
                }
                String line = builder.toString();
                line = line.substring(0, line.length() - 2);
                enchantLore.add(line);
            });
        } else {
            enchantLore.addAll(combinedLore);
        }

        itemLore.addAll(loreStart, enchantLore);

        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta metaBook = (EnchantmentStorageMeta) meta;
            if(!metaBook.getStoredEnchants().equals(((EnchantmentStorageMeta) oldItem.getItemMeta()).getStoredEnchants())) return oldItem;
            forRemoval.forEach((metaBook::removeStoredEnchant));
            metaBook.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS); // Thanks ShaneBee!
            metaBook.addItemFlags(ItemFlag.HIDE_ENCHANTS); // Here just in case
            metaBook.setLore(itemLore);
            metaBook.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, enchantLore.size());
            item.setItemMeta(metaBook);
        } else {
            if(!meta.getEnchants().equals(oldItem.getItemMeta().getEnchants())) return oldItem;
            forRemoval.forEach((meta::removeEnchant));
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(itemLore);
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, enchantLore.size());
            item.setItemMeta(meta);
        }

        return item;
    }
}
