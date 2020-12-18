package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Soulbound;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.events.entitydeathbyentity.EntityDeathByEntityEvent;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.TridentStack;
import com.willfp.ecoenchants.util.internal.DropQueue;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Telekinesis extends EcoEnchant {
    public Telekinesis() {
        super(
                "telekinesis", EnchantmentType.NORMAL
        );
    }

    private static boolean collate = false;
    private static final HashMap<Player, CollatedDrops> COLLATED_MAP = new HashMap<>();
    private static final BukkitRunnable COLLATED_RUNNABLE = new BukkitRunnable() {
        @Override
        public void run() {
            for(Map.Entry<Player, CollatedDrops> entry : COLLATED_MAP.entrySet()) {
                new DropQueue(entry.getKey())
                        .setLocation(entry.getValue().getLocation())
                        .addItems(entry.getValue().getDrops())
                        .push();
            }
        }
    };

    private static BukkitTask collatedRunnableTask = null;

    public void update() {
        collate = this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "collate");
        if(collate) collatedRunnableTask = COLLATED_RUNNABLE.runTaskTimer(EcoEnchantsPlugin.getInstance(), 0, 1);
        else if(collatedRunnableTask != null) Bukkit.getScheduler().cancelTask(collatedRunnableTask.getTaskId());
    }

    // START OF LISTENERS

    // For block drops
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void telekinesisDropItem(BlockDropItemEvent event) {
        Player player = event.getPlayer();

        if (!EnchantChecks.mainhand(player, this)) return;
        if(this.getDisabledWorldNames().contains(player.getWorld().getName())) return;
        //if(this.getDisabledWorlds().contains(player.getWorld())) return;

        if (event.isCancelled()) return;

        Block block = event.getBlock();

        //if (!AntigriefManager.canBreakBlock(player, block)) return;

        List<ItemStack> drops = new ArrayList<>();
        for(Item item : event.getItems()) drops.add(item.getItemStack());

        event.getItems().clear();

        if(collate) collateDropItem(event, player, drops, block);
        else defaultDropItem(event, player, drops, block);
    }

    private void collateDropItem(BlockDropItemEvent event, Player player, List<ItemStack> drops, Block block) {
        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(drops)
                .push();

        player.updateInventory();
    }

    private void defaultDropItem(BlockDropItemEvent event, Player player, List<ItemStack> drops, Block block) {
        CollatedDrops collatedDrops;
        if(COLLATED_MAP.containsKey(player)) {
            HashSet<ItemStack> dropSet = COLLATED_MAP.get(player).getDrops();
            dropSet.addAll(drops);
            collatedDrops = new CollatedDrops(dropSet, block.getLocation());
        } else {
            collatedDrops = new CollatedDrops(new HashSet<>(drops), block.getLocation());
        }

        COLLATED_MAP.put(player, collatedDrops);
    }

    // For exp drops, blockdropitemevent doesn't cover xp
    @EventHandler(priority = EventPriority.HIGH)
    public void telekinesisBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!EnchantChecks.mainhand(player, this)) return;
        if(this.getDisabledWorlds().contains(player.getWorld())) return;

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if (event.isCancelled())
            return;

        if (!AntigriefManager.canBreakBlock(player, block)) return;

        if(block.getType().equals(Material.SPAWNER)) event.setExpToDrop(0);

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addXP(event.getExpToDrop())
                .push();

        event.setExpToDrop(0);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void telekinesisKill(EntityDeathByEntityEvent event) {
        Player player = null;
        LivingEntity entity = event.getVictim();
        ItemStack item = null;

        if(entity instanceof Player && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "not-on-players"))
            return;

        if(event.getKiller() instanceof Player) {
            player = (Player) event.getKiller();
            item = player.getInventory().getItemInMainHand();
        } else if(event.getKiller() instanceof Arrow) {
            if(((Arrow) event.getKiller()).getShooter() instanceof Player) {
                player = (Player) ((Arrow) event.getKiller()).getShooter();
                item = player.getInventory().getItemInMainHand();
            }
        } else if(event.getKiller() instanceof Trident) {
            if(((Trident) event.getKiller()).getShooter() instanceof Player) {
                player = (Player) ((Trident) event.getKiller()).getShooter();
                item = TridentStack.getTridentStack((Trident) event.getKiller());
            }
        }

        if(player == null || item == null) return;

        if (!EnchantChecks.item(item, this)) return;
        if(this.getDisabledWorlds().contains(player.getWorld())) return;

        int xp = event.getDroppedExp();
        Collection<ItemStack> drops = event.getDrops();

        if(entity instanceof Player) {
            if(Soulbound.getSoulboundItems((Player) entity) != null) {
                drops.removeAll(Soulbound.getSoulboundItems((Player) entity));
            }
        }

        new DropQueue(player)
                .addItems(drops)
                .setLocation(entity.getLocation())
                .addXP(xp)
                .forceTelekinesis()
                .push();

        event.getDeathEvent().setDroppedExp(0);
        event.getDeathEvent().getDrops().clear();
    }

    static {
        EcoEnchants.TELEKINESIS.update();
    }

    private static class CollatedDrops {
        private final HashSet<ItemStack> drops;
        private final Location location;

        private CollatedDrops(HashSet<ItemStack> drops, Location location) {
            this.drops = drops;
            this.location = location;
        }

        public HashSet<ItemStack> getDrops() {
            return drops;
        }

        public Location getLocation() {
            return location;
        }
    }
}
