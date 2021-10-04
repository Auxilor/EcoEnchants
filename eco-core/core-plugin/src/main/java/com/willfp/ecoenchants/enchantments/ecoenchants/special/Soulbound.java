package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.NamespacedKey;
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
import org.bukkit.entity.Arrow;

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

        if (!this.areRequirementsMet(player)) {
            return;
        }

        if (!(event.getEntity() instanceof Arrow)) {
            if (player.getKiller() != null) {
                Player killer = player.getKiller();
                int reaperLevel = EnchantChecks.getMainhandLevel(killer, EcoEnchants.REAPER);
                if (reaperLevel > 0) {
                    if (!(EcoEnchants.REAPER.getDisabledWorlds().contains(killer.getWorld()))) {
                        if (EnchantmentUtils.passedChance(EcoEnchants.REAPER, reaperLevel)) {
                            return;
                        }
                    }
                }
            }
        }

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null) {
                continue;
            }

            if (itemStack.containsEnchantment(this)) {
                soulboundItems.add(itemStack);
            }

            if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "on-books")) {
                if (itemStack.getItemMeta() instanceof EnchantmentStorageMeta && (((EnchantmentStorageMeta) itemStack.getItemMeta()).getStoredEnchants().containsKey(this))) {
                    soulboundItems.add(itemStack);
                }
            }
        }

        event.getDrops().removeAll(soulboundItems);

        for (ItemStack itemStack : soulboundItems) {
            ItemMeta meta = itemStack.getItemMeta();
            assert meta != null;
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(this.getPlugin().getNamespacedKeyFactory().create("soulbound"), PersistentDataType.INTEGER, 1);

            if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "remove-after")) {
                if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "on-books")) {
                    if (meta instanceof EnchantmentStorageMeta) {
                        ((EnchantmentStorageMeta) meta).removeStoredEnchant(this);
                    }
                }
                meta.removeEnchant(this);
            }

            itemStack.setItemMeta(meta);
        }

        player.setMetadata("soulbound-items", this.getPlugin().getMetadataValueFactory().create(soulboundItems));
    }

    public boolean hasSoulboundItems(@NotNull final Player player) {
        final NamespacedKey soulbound = this.getPlugin().getNamespacedKeyFactory().create("soulbound");
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && itemStack.getItemMeta().getPersistentDataContainer().has(soulbound, PersistentDataType.INTEGER)) {
                return false;
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSoulboundRespawn(@NotNull final PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        this.getPlugin().getScheduler().run(() -> {
            if (!hasSoulboundItems(player)) {
                return;
            }

            if (!player.hasMetadata("soulbound-items")) {
                return;
            }

            List<ItemStack> soulboundItems = (List<ItemStack>) player.getMetadata("soulbound-items").get(0).value();

            if (soulboundItems == null) {
                player.removeMetadata("soulbound-items", this.getPlugin());
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

            player.removeMetadata("soulbound-items", this.getPlugin());
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(@NotNull final PlayerDeathEvent event) {
        final NamespacedKey soulbound = this.getPlugin().getNamespacedKeyFactory().create("soulbound");
        event.getDrops().removeIf(itemStack -> itemStack.getItemMeta().getPersistentDataContainer().has(soulbound, PersistentDataType.INTEGER));
    }
}
