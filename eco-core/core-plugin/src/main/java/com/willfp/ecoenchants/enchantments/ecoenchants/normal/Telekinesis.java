package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.config.ConfigManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.ecoenchants.special.Soulbound;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.events.entitydeathbyentity.EntityDeathByEntityEvent;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.TridentStack;
import com.willfp.ecoenchants.util.internal.DropQueue;
import org.bukkit.GameMode;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Telekinesis extends EcoEnchant {
    public Telekinesis() {
        super(
                "telekinesis", EnchantmentType.NORMAL
        );
    }

    private static boolean always = false;

    // START OF LISTENERS

    @Override
    protected void postUpdate() {
        always = ConfigManager.getConfig().getBool("drops.force-dropqueue");
    }

    // For block drops
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void telekinesisDropItem(BlockDropItemEvent event) {
        Player player = event.getPlayer();

        if(!always && !EnchantChecks.mainhand(player, this)) return;
        if(this.getDisabledWorldNames().contains(player.getWorld().getName())) return;

        if (event.isCancelled()) return;

        Block block = event.getBlock();

        if (!AntigriefManager.canBreakBlock(player, block)) return;

        List<ItemStack> drops = new ArrayList<>();
        for(Item item : event.getItems()) drops.add(item.getItemStack());

        event.getItems().clear();

        DropQueue queue = new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(drops);
        if(!always) queue.forceTelekinesis();
        queue.push();
    }

    // For exp drops, blockdropitemevent doesn't cover xp
    @EventHandler(priority = EventPriority.HIGH)
    public void telekinesisBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(!always && !EnchantChecks.mainhand(player, this)) return;
        if(this.getDisabledWorlds().contains(player.getWorld())) return;

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
            return;

        if (event.isCancelled())
            return;

        if (!AntigriefManager.canBreakBlock(player, block)) return;

        if(block.getType().equals(Material.SPAWNER)) event.setExpToDrop(0);

        DropQueue queue = new DropQueue(player)
                .setLocation(block.getLocation())
                .addXP(event.getExpToDrop());
        if(!always) queue.forceTelekinesis();
        queue.push();

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
}
