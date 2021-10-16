package com.willfp.ecoenchants.enchantments.support.merging.anvil;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.core.proxy.ProxyConstants;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.proxy.proxies.OpenInventoryProxy;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AnvilListeners extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Map to prevent incrementing cost several times as inventory events are fired 3 times.
     */
    private static final Map<UUID, Integer> ANTI_REPEAT = new HashMap<>();

    /**
     * Class for AnvilGUI wrappers to ignore them.
     */
    private static final String ANVIL_GUI_CLASS = "net.wesjd.anvilgui.version.Wrapper" + ProxyConstants.NMS_VERSION.substring(1) + "$AnvilContainer";

    /**
     * Instantiate anvil listeners and link them to a specific plugin.
     *
     * @param plugin The plugin to link to.
     */
    public AnvilListeners(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called when items are placed into an anvil.
     *
     * @param event The event to listen to.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilPrepare(@NotNull final PrepareAnvilEvent event) {
        /*
        This code is almost as bad as AnvilMerge#doMerge
        Inventory events fire three times so I have to do weird workarounds
        I also don't know how any of this works - so many things are null

        Do I know when the items are changed? No
        Do I know when the experience and name is set? No
        Do I know when the merge has failed? No
        But it works and I won't touch it.

        I wrote this code in July 2020 and I'm amazed that it holds up.
         */


        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);
        ItemStack out = event.getResult();
        String name = event.getInventory().getRenameText();

        if (event.getViewers().isEmpty()) {
            return; // Prevent ArrayIndexOutOfBoundsException when using AnvilGUI
        }

        event.setResult(null);
        event.getInventory().setItem(2, null);

        Player player = (Player) event.getViewers().get(0);
        if (this.getPlugin().getProxy(OpenInventoryProxy.class).getOpenInventory(player).getClass().toString().equals(ANVIL_GUI_CLASS)) {
            return;
        }

        if (name == null) {
            name = "";
        }

        AnvilResult newOut = AnvilMerge.doMerge(left, right, out, name, player);

        if (newOut.result() == null) {
            newOut = new AnvilResult(new ItemStack(Material.AIR, 0), newOut.xp());
        }

        int modCost;
        if (newOut.xp() == null) {
            modCost = 0;
        } else {
            modCost = newOut.xp();
        }

        AnvilResult finalNewOut = newOut;
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
            ItemStack item = finalNewOut.result();

            if (event.getInventory().getItem(0) == null) {
                return;
            }

            if (!Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(item.getType())) {
                return;
            }

            if (this.getPlugin().getConfigYml().getBool("anvil.rework-cost")) {
                int repairCost = FastItemStack.wrap(item).getRepairCost();
                int reworkCount = NumberUtils.log2(repairCost + 1);
                if (repairCost == 0) {
                    reworkCount = 0;
                }
                reworkCount++;
                repairCost = (int) Math.pow(2, reworkCount) - 1;
                FastItemStack fis = FastItemStack.wrap(item);
                fis.setRepairCost(repairCost);
                item = fis.unwrap();
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

            /*
            This is a weird anti-dupe bodge. Don't look into it.
             */
            ItemStack l = event.getInventory().getItem(0);
            Map<Enchantment, Integer> leftEnchants = l == null ? new HashMap<>() : FastItemStack.wrap(l).getEnchantmentsOnItem(true);
            Map<Enchantment, Integer> outEnchants = item == null ? new HashMap<>() : FastItemStack.wrap(item).getEnchantmentsOnItem(true);

            if (event.getInventory().getItem(1) == null
                    && !leftEnchants.equals(outEnchants)) {
                return;
            }

            event.getInventory().setRepairCost(cost);
            event.setResult(item);
            event.getInventory().setItem(2, item);
        });
    }
}
