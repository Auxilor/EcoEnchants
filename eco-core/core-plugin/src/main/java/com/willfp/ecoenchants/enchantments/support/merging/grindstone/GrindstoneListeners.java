package com.willfp.ecoenchants.enchantments.support.merging.grindstone;

import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class GrindstoneListeners extends PluginDependent implements Listener {
    public GrindstoneListeners(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onGrindstone(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.getOpenInventory().getTopInventory().getType() != InventoryType.GRINDSTONE)
            return;

        GrindstoneInventory inventory = (GrindstoneInventory) player.getOpenInventory().getTopInventory();

        this.plugin.getScheduler().runLater(() -> {
            ItemStack top = inventory.getItem(0);
            ItemStack bottom = inventory.getItem(1);
            ItemStack out = inventory.getItem(2);

            Map<Enchantment, Integer> toKeep = GrindstoneMerge.doMerge(top, bottom);

            if (toKeep.isEmpty()) {
                inventory.setItem(2, out);
            }
            if (out == null) return;

            ItemStack newOut = out.clone();
            if (newOut.getItemMeta() instanceof EnchantmentStorageMeta) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) newOut.getItemMeta();
                toKeep.forEach(((enchantment, integer) -> {
                    meta.addStoredEnchant(enchantment, integer, true);
                }));
                newOut.setItemMeta(meta);
            } else {
                ItemMeta meta = newOut.getItemMeta();
                toKeep.forEach(((enchantment, integer) -> {
                    meta.addEnchant(enchantment, integer, true);
                }));
                newOut.setItemMeta(meta);
            }

            final ItemStack finalOut = newOut;

            this.plugin.getScheduler().run(() -> {
                inventory.setItem(2, finalOut);
            });
        }, 1);
    }
}
