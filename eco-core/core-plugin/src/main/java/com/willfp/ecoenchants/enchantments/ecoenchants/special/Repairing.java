package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.TimedRunnable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Repairable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Repairing extends EcoEnchant implements TimedRunnable {
    private final Set<Player> players = new HashSet<>();
    private int amount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "multiplier");

    public Repairing() {
        super(
                "repairing", EnchantmentType.SPECIAL
        );
    }

    @EventHandler
    public void onItemPickup(@NotNull final EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        refreshPlayer((Player) event.getEntity());
    }

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        refresh();
    }

    @EventHandler
    public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        refresh();
    }

    @EventHandler
    public void onInventoryDrop(@NotNull final EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        refreshPlayer((Player) event.getEntity());
    }

    @EventHandler
    public void onInventoryClick(@NotNull final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        refreshPlayer((Player) event.getWhoClicked());
    }

    private void refresh() {
        players.clear();
        this.getPlugin().getScheduler().runLater(() -> this.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            if (Arrays.stream(player.getInventory().getContents()).parallel().anyMatch(item -> EnchantChecks.item(item, this))) {
                if (this.areRequirementsMet(player)) {
                    players.add(player);
                }
            }
        }), 1);
        amount = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "multiplier");
    }

    private void refreshPlayer(@NotNull final Player player) {
        players.remove(player);
        if (Arrays.stream(player.getInventory().getContents()).parallel().anyMatch(item -> EnchantChecks.item(item, this))) {
            if (this.areRequirementsMet(player)) {
                players.add(player);
            }
        }
    }

    @Override
    public void run() {
        players.forEach((player -> {
            if (this.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }
            for (ItemStack item : player.getInventory().getContents()) {
                int level = EnchantChecks.getItemLevel(item, this);
                if (level == 0) {
                    continue;
                }

                if (!(item.getItemMeta() instanceof Repairable)) {
                    continue;
                }

                if (player.getInventory().getItemInMainHand().equals(item)) {
                    continue;
                }

                if (player.getInventory().getItemInOffHand().equals(item)) {
                    continue;
                }

                if (player.getItemOnCursor().equals(item)) {
                    continue;
                }

                DurabilityUtils.repairItem(item, amount * level);
            }
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
