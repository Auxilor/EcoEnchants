package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.eco.util.VectorUtils;
import com.willfp.eco.util.events.armorequip.ArmorEquipEvent;
import com.willfp.eco.util.interfaces.EcoRunnable;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CallingCurse extends EcoEnchant implements EcoRunnable {
    private final HashMap<Player, Integer> players = new HashMap<>();
    private double distance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance");

    public CallingCurse() {
        super(
                "calling_curse", EnchantmentType.CURSE
        );
    }

    @EventHandler
    public void onArmorEquip(@NotNull final ArmorEquipEvent event) {
        refresh();
    }

    @EventHandler
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        refresh();
    }

    @EventHandler
    public void onPlayerLeave(@NotNull final PlayerQuitEvent event) {
        refresh();
    }

    private void refresh() {
        players.clear();
        this.getPlugin().getScheduler().runLater(() -> this.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            int level = EnchantChecks.getArmorPoints(player, this, 0);
            if (level > 0) {
                players.put(player, level);
            }
        }), 1);

        distance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance");
    }

    @Override
    public void run() {
        players.forEach((player, level) -> {
            if (this.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), distance, distance, distance)) {
                if (!(e instanceof Monster)) {
                    continue;
                }

                if (e instanceof PigZombie) {
                    ((PigZombie) e).setAngry(true);
                }

                ((Monster) e).setTarget(player);

                Vector vector = player.getLocation().toVector().clone().subtract(e.getLocation().toVector()).normalize().multiply(0.23d);

                if (VectorUtils.isFinite(vector)) {
                    e.setVelocity(vector);
                }
            }
        });
    }

    @Override
    public long getTime() {
        return this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "repeat-ticks");
    }
}
