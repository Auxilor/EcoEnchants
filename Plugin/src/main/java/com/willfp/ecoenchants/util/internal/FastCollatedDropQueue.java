package com.willfp.ecoenchants.util.internal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastCollatedDropQueue {
    private static boolean collate = false;
    private static final HashMap<Player, CollatedDrops> COLLATED_MAP = new HashMap<>();
    static {
        Bukkit.getScheduler().runTaskTimer(EcoEnchantsPlugin.getInstance(), () -> {
            if (!collate) return;
            for (Map.Entry<Player, CollatedDrops> entry : COLLATED_MAP.entrySet()) {
                new DropQueue(entry.getKey())
                        .setLocation(entry.getValue().getLocation())
                        .addItems(entry.getValue().getDrops())
                        .addXP(entry.getValue().getXp())
                        .push();
            }
            COLLATED_MAP.clear();
        }, 0, 1);

        update();
    }

    public static void collateDrop(Player player, Collection<ItemStack> items, int xp, Location location) {
        CollatedDrops collatedDrops;
        if(COLLATED_MAP.containsKey(player)) {
            List<ItemStack> dropSet = COLLATED_MAP.get(player).getDrops();
            dropSet.addAll(items);
            collatedDrops = new CollatedDrops(dropSet, location, xp);
        } else {
            collatedDrops = new CollatedDrops(new ArrayList<>(items), location, xp);
        }

        COLLATED_MAP.put(player, collatedDrops);
    }

    public static boolean use() {
        return collate;
    }

    public static void update() {
        collate = ConfigManager.getConfig().getBool( "drops.collate");
    }

    private static class CollatedDrops {
        private final List<ItemStack> drops;
        private final Location location;
        private final int xp;

        private CollatedDrops(List<ItemStack> drops, Location location, int xp) {
            this.drops = drops;
            this.location = location;
            this.xp = xp;
        }

        public List<ItemStack> getDrops() {
            return drops;
        }

        public Location getLocation() {
            return location;
        }

        public int getXp() {
            return xp;
        }
    }
}
