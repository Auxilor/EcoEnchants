package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;

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
    private static final HashMap<Player, List<ItemStack>> SOULBOUND_ITEMS = new HashMap<>();

    public static List<ItemStack> getSoulboundItems(@NotNull final Player player) {
        return SOULBOUND_ITEMS.get(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSoulboundDeath(@NotNull final PlayerDeathEvent event) {
        if (event.getKeepInventory()) {
            return;
        }

        Player player = event.getEntity();
        List<ItemStack> soulboundItems = new ArrayList<>(); // Stored as list to preserve duplicates

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).forEach((itemStack -> {
            if (itemStack.containsEnchantment(this)) {
                soulboundItems.add(itemStack);
            }

            if (itemStack.getItemMeta() instanceof EnchantmentStorageMeta && (((EnchantmentStorageMeta) itemStack.getItemMeta()).getStoredEnchants().containsKey(this.getEnchantment()))) {
                soulboundItems.add(itemStack);
            }
        }));

        event.getDrops().removeAll(soulboundItems);

        SOULBOUND_ITEMS.remove(player);
        SOULBOUND_ITEMS.put(player, soulboundItems);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSoulboundRespawn(@NotNull final PlayerRespawnEvent event) {
        if (!SOULBOUND_ITEMS.containsKey(event.getPlayer())) {
            return;
        }

        List<ItemStack> soulboundItems = SOULBOUND_ITEMS.get(event.getPlayer());

        soulboundItems.forEach((itemStack -> {
            if (Arrays.asList(event.getPlayer().getInventory().getContents()).contains(itemStack)) {
                return;
            }

            event.getPlayer().getInventory().addItem(itemStack);
        }));

        this.getPlugin().getScheduler().runLater(() -> SOULBOUND_ITEMS.remove(event.getPlayer()), 1);
    }
}
