package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
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
import org.bukkit.inventory.meta.Repairable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class DecayCurse extends EcoEnchant implements EcoRunnable {
    public DecayCurse() {
        super(
                "decay_curse", EnchantmentType.CURSE
        );
    }

    private final Set<Player> players = new HashSet<>();
    private int amount = 10000;

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        refreshPlayer((Player) event.getEntity());
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
        refreshPlayer((Player) event.getEntity());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player))
            return;
        refreshPlayer((Player) event.getWhoClicked());
    }

    private void refresh() {
        players.clear();
        EcoEnchantsPlugin.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            if(Arrays.stream(player.getInventory().getContents()).parallel().anyMatch(item -> EnchantChecks.item(item, EcoEnchants.DECAY_CURSE)))
                players.add(player);
        });
        amount = EcoEnchants.DECAY_CURSE.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "damage");
    }

    private void refreshPlayer(Player player) {
        players.remove(player);
        if(Arrays.stream(player.getInventory().getContents()).parallel().anyMatch(item -> EnchantChecks.item(item, EcoEnchants.DECAY_CURSE)))
            players.add(player);
    }

    @Override
    public void run() {
        players.forEach((player -> {
            for(ItemStack item : player.getInventory().getContents()) {
                if(!EnchantChecks.item(item, EcoEnchants.DECAY_CURSE)) continue;

                if(!(item.getItemMeta() instanceof Repairable)) continue;

                if(player.getInventory().getItemInMainHand().equals(item)) continue;
                if(player.getInventory().getItemInOffHand().equals(item)) continue;
                if(player.getItemOnCursor().equals(item)) continue;

                DurabilityUtils.damageItemNoBreak(item, amount, player);
            }
        }));
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
