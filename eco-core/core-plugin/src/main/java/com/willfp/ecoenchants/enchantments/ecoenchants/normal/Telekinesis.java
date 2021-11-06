package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.eco.core.events.EntityDeathByEntityEvent;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.core.integrations.mcmmo.McmmoManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.mythicmobs.MythicMobsManager;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Telekinesis extends EcoEnchant {
    private static boolean always = false;

    public Telekinesis() {
        super(
                "telekinesis", EnchantmentType.NORMAL
        );
    }

    @Override
    protected void postUpdate() {
        always = this.getPlugin().getConfigYml().getBool("advanced.force-dropqueue");
    }

    // For block drops
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void telekinesisDropItem(@NotNull final BlockDropItemEvent event) {
        Player player = event.getPlayer();

        if (!always && !(EnchantChecks.mainhand(player, this)) && this.areRequirementsMet(player)) {
            return;
        }

        if (this.getDisabledWorldNames().contains(player.getWorld().getName())) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();

        if (!AntigriefManager.canBreakBlock(player, block)) {
            return;
        }

        List<ItemStack> drops = new ArrayList<>();

        for (Item item : event.getItems()) {
            drops.add(item.getItemStack());
            for (int i = 0; i < McmmoManager.getBonusDropCount(block); i++) {
                drops.add(item.getItemStack());
            }
        }

        event.getItems().clear();

        DropQueue queue = new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(drops);

        if (!always) {
            queue.forceTelekinesis();
        }

        queue.push();
    }

    // For exp drops, blockdropitemevent doesn't cover xp
    @EventHandler(priority = EventPriority.HIGH)
    public void telekinesisBreak(@NotNull final BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!always && !(EnchantChecks.mainhand(player, this)) && this.areRequirementsMet(player)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!AntigriefManager.canBreakBlock(player, block)) {
            return;
        }

        if (block.getType().equals(Material.SPAWNER)) {
            event.setExpToDrop(0);
        }

        DropQueue queue = new DropQueue(player)
                .setLocation(block.getLocation())
                .addXP(event.getExpToDrop());

        if (!always) {
            queue.forceTelekinesis();
        }

        queue.push();

        event.setExpToDrop(0);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void telekinesisKill(@NotNull final EntityDeathByEntityEvent event) {
        Player player = null;
        LivingEntity entity = event.getVictim();
        ItemStack item = null;

        if (entity instanceof Player && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "not-on-players")) {
            return;
        }

        if (!MythicMobsManager.canDropItems(entity)) {
            return;
        }

        if (event.getKiller() instanceof Player) {
            player = (Player) event.getKiller();
            item = player.getInventory().getItemInMainHand();
        } else if (event.getKiller() instanceof Arrow) {
            if (((Arrow) event.getKiller()).getShooter() instanceof Player) {
                player = (Player) ((Arrow) event.getKiller()).getShooter();
                item = player.getInventory().getItemInMainHand();
            }
        } else if (event.getKiller() instanceof Trident trident) {
            if (trident.getShooter() instanceof Player) {
                player = (Player) trident.getShooter();
                item = trident.getItem();
            }
        }

        //noinspection ConstantConditions
        if (player == null || item == null) {
            return;
        }

        if (!EnchantChecks.item(item, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int xp = event.getXp();
        Collection<ItemStack> drops = event.getDrops();

        drops.removeIf(itemStack -> {
            if (itemStack == null) {
                return true;
            }
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) {
                return false;
            }
            //noinspection ConstantConditions
            if (meta.getPersistentDataContainer() == null) {
                return false;
            }
            return meta.getPersistentDataContainer().has(this.getPlugin().getNamespacedKeyFactory().create("soulbound"), PersistentDataType.INTEGER);
        });

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
