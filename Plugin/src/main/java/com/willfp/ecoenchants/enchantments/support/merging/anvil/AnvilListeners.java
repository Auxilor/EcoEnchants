package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.util.tuplets.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class AnvilListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);
        ItemStack out = event.getResult();
        String name = event.getInventory().getRenameText();

        event.setResult(null);
        event.getInventory().setItem(2, null);

        if(event.getViewers().isEmpty()) return; // Prevent ArrayIndexOutOfBoundsException when using AnvilGUI

        Player player = (Player) event.getViewers().get(0);
        if(player.getOpenInventory().getTitle().toLowerCase().contains("quest")) return; // Fix for QuestsGUI

        Pair<ItemStack, Integer> newOut = AnvilMerge.doMerge(left, right, out, name, player);

        if(newOut.getFirst() == null) {
            newOut.setFirst(new ItemStack(Material.AIR, 0));
        }

        int modCost;
        if(newOut.getSecond() == null) {
            modCost = 0;
        } else {
            modCost = newOut.getSecond();
        }

        Bukkit.getScheduler().runTask(EcoEnchantsPlugin.getInstance(), () -> {
            final int cost = modCost + event.getInventory().getRepairCost();
            event.getInventory().setRepairCost(cost);
            event.setResult(newOut.getFirst());
            event.getInventory().setItem(2, newOut.getFirst());
            player.updateInventory();
        });
    }
}