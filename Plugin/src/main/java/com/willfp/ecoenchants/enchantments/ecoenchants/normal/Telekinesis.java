package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.events.entitydeathbyentity.EntityDeathByEntityEvent;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.TridentStack;
import com.willfp.ecoenchants.queue.DropQueue;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Telekinesis extends EcoEnchant {
    public Telekinesis() {
        super(
                new EcoEnchantBuilder("telekinesis", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler(priority = EventPriority.HIGH)
    public void telekinesisDropItem(BlockDropItemEvent event) {
        Player player = event.getPlayer();

        if (!EnchantChecks.mainhand(player, this)) return;

        if (event.isCancelled()) return;

        Block block = event.getBlock();

        if (!AntigriefManager.canBreakBlock(player, block)) return;

        List<ItemStack> drops = new ArrayList<>();
        event.getItems().forEach((item -> {
            drops.add(item.getItemStack());
        }));

        event.getItems().clear();

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(drops)
                .push();

        player.updateInventory();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void telekinesisBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!EnchantChecks.mainhand(player, this)) return;

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

        int xp = event.getDroppedExp();
        Collection<ItemStack> drops = event.getDrops();

        new DropQueue(player)
                .addItems(drops)
                .setLocation(entity.getLocation())
                .addXP(xp)
                .forceTelekinesis()
                .push();

        event.getDeathEvent().setDroppedExp(0);
        event.getDeathEvent().getDrops().clear();
    }
}
