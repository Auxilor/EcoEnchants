package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HoldItemListener extends PluginDependent implements Listener {
    /**
     * Instantiate HoldItemListener.
     *
     * @param plugin Instance of EcoEnchants.
     */
    public HoldItemListener(@NotNull final AbstractEcoPlugin plugin) {
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
    public void onHoldItem(@NotNull final PlayerItemHeldEvent event) {
        ItemStack itemStack = event.getPlayer().getInventory().getItem(event.getNewSlot());

        if (itemStack == null) {
            return;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) {
            return;
        }

        Map<Enchantment, Integer> toAdd = new HashMap<>();

        if (!((EnchantDisplay) this.getPlugin().getDisplayModule()).getOptions().isUseLoreGetter()) {
            return;
        }

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

                    String enchantName = lineSplit.stream().collect(Collectors.joining(" "));
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
}
