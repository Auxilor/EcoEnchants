package com.willfp.eco.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class DurabilityUtils {
    /**
     * Damage an item in a player's inventory.
     * The slot of a held item can be obtained with {@link PlayerInventory#getHeldItemSlot()}.
     * Armor slots are 39 (helmet), 38 (chestplate), 37 (leggings), 36 (boots).
     *
     * @param player The player.
     * @param item   The item to damage.
     * @param damage The amount of damage to deal.
     * @param slot   The slot in the inventory of the item.
     */
    public void damageItem(@NotNull final Player player,
                           @NotNull final ItemStack item,
                           final int damage,
                           final int slot) {
        if (item.getItemMeta() == null) {
            return;
        }

        if (item.getItemMeta().isUnbreakable()) {
            return;
        }

        PlayerItemDamageEvent event3 = new PlayerItemDamageEvent(player, item, damage);
        Bukkit.getPluginManager().callEvent(event3);

        if (!event3.isCancelled()) {
            int damage2 = event3.getDamage();
            if (item.getItemMeta() instanceof Damageable) {
                Damageable meta = (Damageable) item.getItemMeta();
                meta.setDamage(meta.getDamage() + damage2);

                if (meta.getDamage() >= item.getType().getMaxDurability()) {
                    meta.setDamage(item.getType().getMaxDurability());
                    item.setItemMeta((ItemMeta) meta);
                    PlayerItemBreakEvent event = new PlayerItemBreakEvent(player, item);
                    Bukkit.getPluginManager().callEvent(event);
                    player.getInventory().clear(slot);
                    player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1, 1);
                } else {
                    item.setItemMeta((ItemMeta) meta);
                }
            }
        }
    }

    /**
     * Damage an item in a player's inventory without breaking it.
     *
     * @param item   The item to damage.
     * @param damage The amount of damage to deal.
     * @param player The player.
     */
    public void damageItemNoBreak(@NotNull final ItemStack item,
                                  final int damage,
                                  @NotNull final Player player) {
        if (item.getItemMeta() == null) {
            return;
        }

        if (item.getItemMeta().isUnbreakable()) {
            return;
        }

        PlayerItemDamageEvent event3 = new PlayerItemDamageEvent(player, item, damage);
        Bukkit.getPluginManager().callEvent(event3);

        if (!event3.isCancelled()) {
            int damage2 = event3.getDamage();
            if (item.getItemMeta() instanceof Damageable) {
                Damageable meta = (Damageable) item.getItemMeta();
                meta.setDamage(meta.getDamage() + damage2);

                if (meta.getDamage() >= item.getType().getMaxDurability()) {
                    meta.setDamage(item.getType().getMaxDurability() - 1);
                }
                item.setItemMeta((ItemMeta) meta);
            }
        }
    }

    /**
     * Repair an item in a player's inventory.
     *
     * @param item   The item to damage.
     * @param repair The amount of damage to heal.
     */
    public void repairItem(@NotNull final ItemStack item,
                           final int repair) {
        if (item.getItemMeta() == null) {
            return;
        }

        if (item.getItemMeta().isUnbreakable()) {
            return;
        }

        if (item.getItemMeta() instanceof Damageable) {
            Damageable meta = (Damageable) item.getItemMeta();
            meta.setDamage(meta.getDamage() - repair);

            if (meta.getDamage() < 0) {
                meta.setDamage(0);
            }
            item.setItemMeta((ItemMeta) meta);
        }
    }
}
