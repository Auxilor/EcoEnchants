package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import com.willfp.eco.core.proxy.ProxyConstants;
import com.willfp.eco.core.proxy.ProxyFactory;
import com.willfp.eco.core.proxy.proxies.OpenInventoryProxy;
import com.willfp.eco.core.proxy.proxies.RepairCostProxy;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.tuplets.Pair;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AnvilListeners extends PluginDependent implements Listener {
    private static final HashMap<UUID, Integer> noIncreaseXpMap = new HashMap<>();
    private static final String ANVIL_GUI_CLASS = "net.wesjd.anvilgui.version.Wrapper" + ProxyConstants.NMS_VERSION.substring(1) + "$AnvilContainer";

    protected AnvilListeners(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);
        ItemStack out = event.getResult();
        String name = event.getInventory().getRenameText();

        event.setResult(null);
        event.getInventory().setItem(2, null);

        if (event.getViewers().isEmpty()) return; // Prevent ArrayIndexOutOfBoundsException when using AnvilGUI

        Player player = (Player) event.getViewers().get(0);
        if (new ProxyFactory<>(OpenInventoryProxy.class).getProxy().getOpenInventory(player).getClass().toString().equals(ANVIL_GUI_CLASS))
            return;

        Pair<ItemStack, Integer> newOut = AnvilMerge.doMerge(left, right, out, name, player);

        if (newOut.getFirst() == null) {
            newOut.setFirst(new ItemStack(Material.AIR, 0));
        }

        int modCost;
        if (newOut.getSecond() == null) {
            modCost = 0;
        } else {
            modCost = newOut.getSecond();
        }

        this.plugin.getScheduler().run(() -> {

            // This is a disgusting bodge
            if (!noIncreaseXpMap.containsKey(player.getUniqueId()))
                noIncreaseXpMap.put(player.getUniqueId(), 0);

            Integer num = noIncreaseXpMap.get(player.getUniqueId());
            num += 1;
            noIncreaseXpMap.put(player.getUniqueId(), num);

            Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
                noIncreaseXpMap.remove(player.getUniqueId());
            }, 1);
            // End pain

            int preCost = event.getInventory().getRepairCost();
            ItemStack item = newOut.getFirst();

            if (event.getInventory().getItem(0) == null) {
                return;
            }

            if (!Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(item.getType())) return;

            if (Configs.CONFIG.getBool("anvil.rework-cost")) {
                int repairCost = new ProxyFactory<>(RepairCostProxy.class).getProxy().getRepairCost(item);
                int reworkCount = NumberUtils.log2(repairCost + 1);
                if (repairCost == 0) reworkCount = 0;
                reworkCount++;
                repairCost = (int) Math.pow(2, reworkCount) - 1;
                item = new ProxyFactory<>(RepairCostProxy.class).getProxy().setRepairCost(item, repairCost);
            }

            int cost;

            if (noIncreaseXpMap.get(player.getUniqueId()) == 1) {
                cost = preCost + modCost;
            } else {
                cost = preCost;
            }

            if (!Objects.equals(left, player.getOpenInventory().getItem(0))) {
                return;
            }
            if (cost == 0)
                return;

            event.getInventory().setRepairCost(cost);
            event.setResult(item);
            event.getInventory().setItem(2, item);
            player.updateInventory();
        });
    }
}