package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.VectorUtils;
import com.willfp.eco.util.events.armorequip.ArmorEquipEvent;
import com.willfp.eco.util.interfaces.EcoRunnable;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Magnetic extends EcoEnchant implements EcoRunnable {
    public Magnetic() {
        super(
                "magnetic", EnchantmentType.NORMAL
        );
    }

    private final HashMap<Player, Integer> players = new HashMap<>();
    private double initialDistance = 1;
    private double bonus = 1;

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
        this.getPlugin().getServer().getOnlinePlayers().forEach(player -> {
            int level = EnchantChecks.getArmorPoints(player, this, 0);
            if (level > 0) {
                players.put(player, level);
            }
        });
        initialDistance = EcoEnchants.MAGNETIC.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "initial-distance");
        bonus = EcoEnchants.MAGNETIC.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "bonus-per-level");
    }

    @Override
    public void run() {
        players.forEach((player, level) -> {
            double distance = initialDistance + (level * bonus);
            if (this.getDisabledWorlds().contains(player.getWorld())) {
                return;
            }

            for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), distance, 2.0d, distance)) {
                if (!(e instanceof Item || e instanceof ExperienceOrb)) {
                    continue;
                }

                if (e instanceof Item && ((Item) e).getPickupDelay() > 0) {
                    continue;
                }

                Vector vector = player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.1 * level);

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
