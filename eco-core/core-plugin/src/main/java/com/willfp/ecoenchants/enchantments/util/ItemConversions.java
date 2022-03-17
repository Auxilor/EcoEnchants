package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentTarget;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class ItemConversions extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Pass an {@link EcoPlugin} in order to interface with it.
     *
     * @param plugin The plugin to manage.
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
        if (!ItemConversionOptions.isUsingLoreGetter()) {
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
        if (!ItemConversionOptions.isUsingAggressiveLoreGetter()) {
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
        if (!ItemConversionOptions.isUsingExperimentalHideFixer()) {
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
        if (!ItemConversionOptions.isUsingAggressiveExperimentalHideFixer()) {
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

        if (ItemConversionOptions.isUsingForceHideFixer()) {
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
        if (!ItemConversionOptions.isUsingLevelClamp()) {
            return;
        }

        if (event.getPlayer().hasPermission("ecoenchants.bypasslevelclamp")) {
            return;
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();

        clampItemLevels(itemStack, event.getPlayer());
    }

    private void clampItemLevels(@Nullable final ItemStack itemStack,
                                 @NotNull final Player player) {
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

        Map<Enchantment, Integer> levels = FastItemStack.wrap(itemStack).getEnchants(true);

        if (meta instanceof EnchantmentStorageMeta storageMeta) {
            levels.forEach((enchantment, integer) -> {
                if (integer > enchantment.getMaxLevel()) {
                    storageMeta.removeStoredEnchant(enchantment);
                    storageMeta.addStoredEnchant(enchantment, enchantment.getMaxLevel(), true);
                }
            });
        } else {
            levels.forEach((enchantment, integer) -> {
                if (integer > enchantment.getMaxLevel()) {
                    meta.removeEnchant(enchantment);
                    meta.addEnchant(enchantment, enchantment.getMaxLevel(), true);
                }
            });
        }

        itemStack.setItemMeta(meta);

        if (ItemConversionOptions.isUsingLevelClampDelete()) {
            itemStack.setType(Material.AIR);
            itemStack.setAmount(0);
            Bukkit.getLogger().warning(player.getName() + " has/had an illegal item!");
        }
    }

    /**
     * On player hold item.
     * <p>
     * Listener for hide fixer.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void removeDisallowed(@NotNull final PlayerItemHeldEvent event) {
        if (!ItemConversionOptions.isRemovingIllegal()) {
            return;
        }

        if (event.getPlayer().hasPermission("ecoenchants.allowillegal")) {
            return;
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        clampItemLevels(itemStack, event.getPlayer());
    }

    private void removeDisallowed(@Nullable final ItemStack itemStack,
                                  @NotNull final Player player) {
        if (itemStack == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        if (meta instanceof EnchantmentStorageMeta) {
            return;
        }

        boolean illegal = false;

        EcoEnchant illegalEnchant = null;

        for (Enchantment enchantment : meta.getEnchants().keySet()) {
            if (enchantment instanceof EcoEnchant enchant) {
                if (!enchant.getTargetMaterials().contains(itemStack.getType())) {
                    illegal = true;
                    illegalEnchant = enchant;
                }
            }
        }

        if (!illegal) {
            return;
        }

        if (ItemConversionOptions.isDeletingIllegal()) {
            itemStack.setType(Material.AIR);
            itemStack.setItemMeta(new ItemStack(Material.AIR).getItemMeta());
            itemStack.setItemMeta(meta);
        } else {
            meta.removeEnchant(illegalEnchant);
        }

        Bukkit.getLogger().warning(player.getName() + " has/had an illegal item!");
    }


    /**
     * On player hold item.
     * <p>
     * Listener for conversion.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void invalidRemover(@NotNull final PlayerItemHeldEvent event) {
        if (!ItemConversionOptions.isRemoveDisabled()) {
            return;
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        fixInvalid(itemStack);
    }

    private void fixInvalid(@Nullable final ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        Map<Enchantment, Integer> enchants = FastItemStack.wrap(itemStack).getEnchantmentsOnItem(true);

        for (Enchantment enchantment : new HashSet<>(enchants.keySet())) {
            if (enchantment instanceof EcoEnchant enchant) {
                if (!enchant.isEnabled()) {
                    enchants.remove(enchantment);
                }
            }
        }

        if (meta instanceof EnchantmentStorageMeta storageMeta) {
            storageMeta.getStoredEnchants().forEach((enchantment, integer) -> {
                storageMeta.removeStoredEnchant(enchantment);
            });

            enchants.forEach((enchantment, integer) -> {
                storageMeta.addStoredEnchant(enchantment, integer, true);
            });
        } else {
            meta.getEnchants().forEach((enchantment, integer) -> {
                meta.removeEnchant(enchantment);
            });

            enchants.forEach((enchantment, integer) -> {
                meta.addEnchant(enchantment, integer, true);
            });
        }

        itemStack.setItemMeta(meta);
    }

    /**
     * On player hold item.
     * <p>
     * Listener for conversion.
     *
     * @param event The event to listen for.
     */
    @EventHandler
    public void hardCapClamp(@NotNull final PlayerItemHeldEvent event) {
        if (!ItemConversionOptions.isUsingHardCapClamp()) {
            return;
        }


        if (!this.getPlugin().getConfigYml().getBool("anvil.hard-cap.enabled")) {
            return;
        }

        if (event.getPlayer().hasPermission("ecoenchants.anvil.bypasshardcap")) {
            return;
        }

        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        clampHardCap(itemStack);
    }

    private void clampHardCap(@Nullable final ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        Map<Enchantment, Integer> enchants = FastItemStack.wrap(itemStack).getEnchantmentsOnItem(true);
        Map<Enchantment, Integer> replacement = new HashMap<>();

        int i = 0;
        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            if (i >= this.getPlugin().getConfigYml().getInt("anvil.hard-cap.cap")) {
                return;
            }

            Enchantment enchantment = entry.getKey();
            if (enchantment instanceof EcoEnchant enchant) {
                if (!enchant.hasFlag("hard-cap-ignore")) {
                    i++;
                    replacement.put(entry.getKey(), entry.getValue());
                }
            } else {
                i++;
                replacement.put(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment instanceof EcoEnchant enchant) {
                if (enchant.hasFlag("hard-cap-ignore")) {
                    replacement.put(entry.getKey(), entry.getValue());
                }
            }
        }

        if (meta instanceof EnchantmentStorageMeta storageMeta) {
            storageMeta.getStoredEnchants().forEach((enchantment, integer) -> {
                storageMeta.removeStoredEnchant(enchantment);
            });

            replacement.forEach((enchantment, integer) -> {
                storageMeta.addStoredEnchant(enchantment, integer, true);
            });
        } else {
            meta.getEnchants().forEach((enchantment, integer) -> {
                meta.removeEnchant(enchantment);
            });

            replacement.forEach((enchantment, integer) -> {
                meta.addEnchant(enchantment, integer, true);
            });
        }

        itemStack.setItemMeta(meta);
    }
}
