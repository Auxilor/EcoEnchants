package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
public class Soulbound extends EcoEnchant {
    public Soulbound() {
        super(
                "soulbound", EnchantmentType.SPECIAL
        );
    }

    // START OF LISTENERS

    private static final HashMap<Player, List<ItemStack>> soulboundItemsMap = new HashMap<>();

    public static List<ItemStack> getSoulboundItems(Player player) {
        return soulboundItemsMap.get(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSoulboundDeath(PlayerDeathEvent event) {
        if(event.getKeepInventory()) return;

        Player player = event.getEntity();
        List<ItemStack> soulboundItems = new ArrayList<>(); // Stored as list to preserve duplicates

        if(this.getDisabledWorlds().contains(player.getWorld())) return;

        Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).forEach((itemStack -> {
            if(itemStack.containsEnchantment(this)) soulboundItems.add(itemStack);

            if(itemStack.getItemMeta() instanceof EnchantmentStorageMeta) {
                if(((EnchantmentStorageMeta) itemStack.getItemMeta()).getStoredEnchants().containsKey(this)) soulboundItems.add(itemStack);
            }
        }));

        event.getDrops().removeAll(soulboundItems);

        soulboundItemsMap.remove(player);
        soulboundItemsMap.put(player, soulboundItems);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSoulboundRespawn(PlayerRespawnEvent event) {
        if(!soulboundItemsMap.containsKey(event.getPlayer())) return;

        List<ItemStack> soulboundItems = soulboundItemsMap.get(event.getPlayer());

        soulboundItems.forEach((itemStack -> {
            if(Arrays.asList(event.getPlayer().getInventory().getContents()).contains(itemStack)) return;

            event.getPlayer().getInventory().addItem(itemStack);
        }));

        Bukkit.getScheduler().runTaskLater(EcoEnchantsPlugin.getInstance(), () -> {
            soulboundItemsMap.remove(event.getPlayer());
        }, 1);
    }
}
