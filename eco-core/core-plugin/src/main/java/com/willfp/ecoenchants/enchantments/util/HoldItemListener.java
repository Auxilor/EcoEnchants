package com.willfp.ecoenchants.enchantments.util;

import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.display.EnchantDisplay;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HoldItemListener implements Listener {

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

        if (EnchantDisplay.OPTIONS.isUseLoreGetter()) {
            List<String> lore = meta.getLore();

            if (lore == null) {
                lore = new ArrayList<>();
            }

            for (String line : new ArrayList<>(lore)) {
                String rawLine = line;
                line = ChatColor.stripColor(line);

                EcoEnchant enchant;
                int level;
                String[] lineSplit = line.split(" ");
                if (lineSplit.length == 1) {
                    enchant = EcoEnchants.getByName(lineSplit[0]);
                    level = 1;
                } else {
                    String levelString = lineSplit[lineSplit.length - 1];
                    levelString = levelString.replace(" ", "");

                    try {
                        level = NumberUtils.fromNumeral(levelString);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }

                    StringBuilder nameBuilder = new StringBuilder();

                    for (int i = 0; i < lineSplit.length - 1; i++) {
                        nameBuilder.append(lineSplit[i]);
                        if (i != lineSplit.length - 2) {
                            nameBuilder.append(" ");
                        }
                    }

                    enchant = EcoEnchants.getByName(nameBuilder.toString());
                }

                if (enchant != null) {
                    lore.remove(rawLine);
                    meta.addEnchant(enchant, level, true);
                }
            }

            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }
    }
}
