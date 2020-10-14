package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
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
public final class Soulbound extends EcoEnchant {
    public Soulbound() {
        super(
                new EcoEnchantBuilder("soulbound", EnchantmentType.SPECIAL, 5.0)
        );
    }

    // START OF LISTENERS

    HashMap<Player, List<ItemStack>> soulboundItemsMap = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onSoulboundDeath(PlayerDeathEvent event) {
        if(event.getKeepInventory()) return;

        Player player = event.getEntity();
        List<ItemStack> soulboundItems = new ArrayList<>(); // Stored as list to preserve duplicates

        Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).forEach((itemStack -> {
            if(itemStack.containsEnchantment(this)) soulboundItems.add(itemStack);

            if(itemStack.getItemMeta() instanceof EnchantmentStorageMeta) {
                if(((EnchantmentStorageMeta) itemStack.getItemMeta()).getStoredEnchants().containsKey(this)) soulboundItems.add(itemStack);
            }
        }));

        event.getDrops().removeAll(soulboundItems);

        soulboundItemsMap.put(player, soulboundItems);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSoulboundRespawn(PlayerRespawnEvent event) {
        if(!soulboundItemsMap.containsKey(event.getPlayer())) return;

        List<ItemStack> soulboundItems = soulboundItemsMap.get(event.getPlayer());

        soulboundItems.forEach((itemStack -> {
            if(Arrays.asList(event.getPlayer().getInventory().getContents()).contains(itemStack)) return;

            event.getPlayer().getInventory().addItem(itemStack);
        }));
        soulboundItemsMap.remove(event.getPlayer());
    }
}
