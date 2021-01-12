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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Soulbound extends EcoEnchant {
    public Soulbound() {
        super(
                "soulbound", EnchantmentType.SPECIAL
        );
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onSoulboundDeath(@NotNull final PlayerDeathEvent event) {
        if (event.getKeepInventory()) {
            return;
        }

        Player player = event.getEntity();
        List<ItemStack> soulboundItems = new ArrayList<>(); // Stored as list to preserve duplicates

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) {
                continue;
            }

            if (itemStack.containsEnchantment(this)) {
                soulboundItems.add(itemStack);
            }

            if (itemStack.getItemMeta() instanceof EnchantmentStorageMeta && (((EnchantmentStorageMeta) itemStack.getItemMeta()).getStoredEnchants().containsKey(this.getEnchantment()))) {
                soulboundItems.add(itemStack);
            }
        }

        event.getDrops().removeAll(soulboundItems);

        for (ItemStack itemStack : soulboundItems) {
            ItemMeta meta = itemStack.getItemMeta();
            assert meta != null;
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(this.getPlugin().getNamespacedKeyFactory().create("soulbound"), PersistentDataType.INTEGER, 1);
            itemStack.setItemMeta(meta);
        }

        player.setMetadata("soulbound-items", this.getPlugin().getMetadataValueFactory().create(soulboundItems));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSoulboundRespawn(@NotNull final PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        this.getPlugin().getScheduler().runLater(() -> {
            List<ItemStack> soulboundItems = (List<ItemStack>) player.getMetadata("soulbound-items").get(0).value();

            if (soulboundItems == null) {
                return;
            }

            for (ItemStack soulboundItem : soulboundItems) {
                player.getInventory().remove(soulboundItem);

                ItemMeta meta = soulboundItem.getItemMeta();
                assert meta != null;
                meta.getPersistentDataContainer().remove(this.getPlugin().getNamespacedKeyFactory().create("soulbound"));
                soulboundItem.setItemMeta(meta);
                player.getInventory().addItem(soulboundItem);
            }
        }, 1);
    }
}
