package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.events.armorequip.ArmorEquipEvent;
import com.willfp.ecoenchants.util.VectorUtils;
import com.willfp.ecoenchants.util.interfaces.EcoRunnable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;

public final class CallingCurse extends EcoEnchant implements EcoRunnable {
    public CallingCurse() {
        super(
                new EcoEnchantBuilder("calling_curse", EnchantmentType.CURSE)
        );
    }
    private final HashMap<Player, Integer> players = new HashMap<>();

    @EventHandler
    public void onArmorEquip(ArmorEquipEvent event) {
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

    private void refresh() {
        players.clear();
        EcoEnchantsPlugin.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            int level = EnchantChecks.getArmorPoints(player, this, 0);
            if(level > 0) {
                players.put(player, level);
            }
        });
    }

    @Override
    public void run() {
        players.forEach((player, level) -> {
            double distance = EcoEnchants.CALLING_CURSE.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "distance");

            for (Entity e : player.getWorld().getNearbyEntities(player.getLocation(), distance, distance, distance)) {
                if(!(e instanceof Monster)) continue;

                if(e instanceof PigZombie) {
                    ((PigZombie) e).setAngry(true);
                }

                ((Monster) e).setTarget(player);

                Vector vector = player.getLocation().toVector().clone().subtract(e.getLocation().toVector()).normalize().multiply(0.23d);

                if(VectorUtils.isFinite(vector)) {
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
