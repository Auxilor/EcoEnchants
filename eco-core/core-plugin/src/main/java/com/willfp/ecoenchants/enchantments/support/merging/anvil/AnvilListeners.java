package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import com.willfp.eco.core.proxy.ProxyConstants;
import com.willfp.eco.core.proxy.proxies.OpenInventoryProxy;
import com.willfp.eco.core.proxy.proxies.RepairCostProxy;
import com.willfp.eco.util.NumberUtils;
import com.willfp.eco.util.ProxyUtils;
import com.willfp.eco.util.config.Configs;
import com.willfp.eco.util.internal.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.tuplets.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AnvilListeners extends PluginDependent implements Listener {
    /**
     * Map to prevent incrementing cost several times as inventory events are fired 3 times.
     */
    private static final HashMap<UUID, Integer> ANTI_REPEAT = new HashMap<>();

    /**
     * Class for AnvilGUI wrappers to ignore them.
     */
    private static final String ANVIL_GUI_CLASS = "net.wesjd.anvilgui.version.Wrapper" + ProxyConstants.NMS_VERSION.substring(1) + "$AnvilContainer";

    /**
     * Instantiate anvil listeners and link them to a specific plugin.
     *
     * @param plugin The plugin to link to.
     */
    public AnvilListeners(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called when items are placed into an anvil.
     *
     * @param event The event to listen to.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilPrepare(@NotNull final PrepareAnvilEvent event) {
        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);
        ItemStack out = event.getResult();
        String name = event.getInventory().getRenameText();

        event.setResult(null);
        event.getInventory().setItem(2, null);

        if (event.getViewers().isEmpty()) {
            return; // Prevent ArrayIndexOutOfBoundsException when using AnvilGUI
        }

        Player player = (Player) event.getViewers().get(0);
        if (ProxyUtils.getProxy(OpenInventoryProxy.class).getOpenInventory(player).getClass().toString().equals(ANVIL_GUI_CLASS)) {
            return;
        }

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

        this.getPlugin().getScheduler().run(() -> {

            // This is a disgusting bodge
            if (!ANTI_REPEAT.containsKey(player.getUniqueId())) {
                ANTI_REPEAT.put(player.getUniqueId(), 0);
            }

            Integer num = ANTI_REPEAT.get(player.getUniqueId());
            num += 1;
            ANTI_REPEAT.put(player.getUniqueId(), num);

            this.getPlugin().getScheduler().runLater(() -> ANTI_REPEAT.remove(player.getUniqueId()), 1);

            // End pain

            int preCost = event.getInventory().getRepairCost();
            ItemStack item = newOut.getFirst();

            if (event.getInventory().getItem(0) == null) {
                return;
            }

            if (!Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(item.getType())) {
                return;
            }

            if (Configs.CONFIG.getBool("anvil.rework-cost")) {
                int repairCost = ProxyUtils.getProxy(RepairCostProxy.class).getRepairCost(item);
                int reworkCount = NumberUtils.log2(repairCost + 1);
                if (repairCost == 0) {
                    reworkCount = 0;
                }
                reworkCount++;
                repairCost = (int) Math.pow(2, reworkCount) - 1;
                item = ProxyUtils.getProxy(RepairCostProxy.class).setRepairCost(item, repairCost);
            }

            int cost;

            if (ANTI_REPEAT.get(player.getUniqueId()) == 1) {
                cost = preCost + modCost;
            } else {
                cost = preCost;
            }

            if (!Objects.equals(left, player.getOpenInventory().getItem(0))) {
                return;
            }
            if (cost == 0) {
                return;
            }

            event.getInventory().setRepairCost(cost);
            event.setResult(item);
            event.getInventory().setItem(2, item);
            player.updateInventory();
        });
    }
}
