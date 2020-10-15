package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.DurabilityUtils;
import com.willfp.ecoenchants.util.interfaces.EcoRunnable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Repairing extends EcoEnchant implements EcoRunnable {
    public Repairing() {
        super(
                new EcoEnchantBuilder("repairing", EnchantmentType.SPECIAL)
        );
    }

    private final Set<Player> players = new HashSet<>();
    private int multiplier;

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        refresh();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        refresh();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        refresh();
    }

    @EventHandler
    public void onInventoryDrop(EntityDropItemEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        refresh();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        refresh();
    }

    private void refresh() {
        players.clear();
        EcoEnchantsPlugin.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            if(Arrays.stream(player.getInventory().getContents()).parallel().anyMatch(item -> EnchantChecks.item(item, EcoEnchants.REPAIRING)))
                players.add(player);
        });
        multiplier = EcoEnchants.REPAIRING.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "multiplier");
    }

    @Override
    public void run() {
        players.forEach((player -> {
            Arrays.stream(player.getInventory().getContents())
                    .filter(ItemStack::hasItemMeta)
                    .filter(item -> item.getItemMeta().hasEnchant(EcoEnchants.REPAIRING))
                    .filter(item -> player.getInventory().getItemInOffHand().equals(item))
                    .filter(item -> player.getInventory().getItemInMainHand().equals(item))
                    .filter(item -> player.getItemOnCursor().equals(item))
                    .forEach(item -> {
                        int level = EnchantChecks.getItemLevel(item, EcoEnchants.REPAIRING);
                        int repairAmount = level * multiplier;
                        DurabilityUtils.repairItem(item, repairAmount);
                    }
            );
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
