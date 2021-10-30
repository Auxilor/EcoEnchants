package com.willfp.ecoenchants.enchantments.support.merging.grindstone;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginDependent;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GrindstoneListeners extends PluginDependent<EcoPlugin> implements Listener {
    /**
     * Instantiate grindstone listeners and link them to a specific plugin.
     *
     * @param plugin The plugin to link to.
     */
    public GrindstoneListeners(@NotNull final EcoPlugin plugin) {
        super(plugin);
    }

    /**
     * Called when items are grindstoned.
     *
     * @param event The event to listen to.
     */
    @EventHandler
    public void onGrindstone(@NotNull final InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.getOpenInventory().getTopInventory().getType() != InventoryType.GRINDSTONE) {
            return;
        }
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }

        GrindstoneInventory inventory = (GrindstoneInventory) player.getOpenInventory().getTopInventory();

        ItemStack top = inventory.getItem(0);
        ItemStack bottom = inventory.getItem(1);
        ItemStack out = inventory.getItem(2);

        if (out == null) {
            return;
        }

        Map<Enchantment, Integer> merged = GrindstoneMerge.doMerge(top, bottom);
        ItemMeta meta = out.getItemMeta();
        for (Enchantment enchantment : merged.keySet()) {
            if (meta instanceof EnchantmentStorageMeta storageMeta) {
                storageMeta.addStoredEnchant(enchantment, merged.get(enchantment), true);
            } else {
                meta.addEnchant(enchantment, merged.get(enchantment), true);
            }
        }
        out.setItemMeta(meta);

        this.getPlugin().getScheduler().runLater(() -> {
            if (inventory.getItem(2) != null || event.isCancelled()) return;
            Set<Enchantment> enchants = new HashSet<>();
            if (top != null) {
                enchants.addAll(top.getEnchantments().keySet());
            }
            if (bottom != null) {
                enchants.addAll(bottom.getEnchantments().keySet());
            }
            enchants.removeIf(enchantment -> !(enchantment instanceof EcoEnchant) || merged.containsKey(enchantment));
            if (!enchants.isEmpty()) {
                Location loc = player.getLocation().clone().add(
                        NumberUtils.randFloat(-1, 1),
                        NumberUtils.randFloat(-1, 1),
                        NumberUtils.randFloat(-1, 1)
                );
                ExperienceOrb orb = (ExperienceOrb) loc.getWorld().spawnEntity(loc, EntityType.EXPERIENCE_ORB);
                orb.setExperience(enchants.size() * 15);
            }
        }, 1);
    }

    /**
     * An additional grindstone listener for isGrindstoneable check.
     *
     * @param event The event to listen to.
     */
    @EventHandler
    public void onGrindstoneFix(@NotNull final InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.getOpenInventory().getTopInventory().getType() != InventoryType.GRINDSTONE) {
            return;
        }
        this.getPlugin().getScheduler().runLater(() -> {
            GrindstoneInventory inventory = (GrindstoneInventory) player.getOpenInventory().getTopInventory();

            ItemStack top = inventory.getItem(0);
            ItemStack bottom = inventory.getItem(1);
            ItemStack out = inventory.getItem(2);

            if (out == null) {
                return;
            }

            Map<Enchantment, Integer> merged = GrindstoneMerge.doMerge(top, bottom);
            ItemMeta meta = out.getItemMeta();
            for (Enchantment enchantment : merged.keySet()) {
                if (meta instanceof EnchantmentStorageMeta storageMeta) {
                    storageMeta.addStoredEnchant(enchantment, merged.get(enchantment), true);
                } else {
                    meta.addEnchant(enchantment, merged.get(enchantment), true);
                }
            }
            out.setItemMeta(meta);
        }, 1);
    }

}
