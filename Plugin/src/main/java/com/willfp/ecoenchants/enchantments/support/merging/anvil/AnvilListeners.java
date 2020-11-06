package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.nms.RepairCost;
import com.willfp.ecoenchants.util.EcoBukkitRunnable;
import com.willfp.ecoenchants.util.NumberUtils;
import com.willfp.ecoenchants.util.tuplets.Pair;
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

        new EcoBukkitRunnable(player.getTicksLived()) {
            @Override
            public void onRun() {
                int preCost = event.getInventory().getRepairCost();
                ItemStack item = newOut.getFirst();

                if(ConfigManager.getConfig().getBool("anvil.rework-cost")) {
                    int repairCost = RepairCost.getRepairCost(item);
                    int reworkCount = NumberUtils.log2(repairCost + 1);
                    if (repairCost == 0) reworkCount = 0;
                    reworkCount++;
                    repairCost = (int) Math.pow(2, reworkCount) - 1;
                    item = RepairCost.setRepairCost(item, repairCost);
                }

                int cost = preCost + modCost;

                event.getInventory().setRepairCost(cost);
                event.setResult(item);
                event.getInventory().setItem(2, item);
                player.updateInventory();
            }
        }.runTask(EcoEnchantsPlugin.getInstance());
    }
}