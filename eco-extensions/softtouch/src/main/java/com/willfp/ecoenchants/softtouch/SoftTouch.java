package com.willfp.ecoenchants.softtouch;


import com.willfp.eco.core.drops.DropQueue;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SoftTouch extends EcoEnchant {
    public SoftTouch() {
        super("soft_touch", EnchantmentType.NORMAL);
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @SuppressWarnings("checkstyle:OperatorWrap")
    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (!(block.getState() instanceof CreatureSpawner spawner)) {
            return;
        }

        EntityType type = spawner.getSpawnedType();

        event.setDropItems(false);
        event.setExpToDrop(0);

        ItemStack itemStack = new ItemStack(Material.SPAWNER);
        BlockStateMeta meta = (BlockStateMeta) itemStack.getItemMeta();
        assert meta != null;
        spawner.setSpawnedType(type);
        meta.setBlockState(spawner);
        meta.getPersistentDataContainer().set(this.getPlugin().getNamespacedKeyFactory().create("softtouch"), PersistentDataType.STRING, type.name());

        String entityName = displayNicely(type);

        String name = this.getConfig().getFormattedString(EcoEnchants.CONFIG_LOCATION + "name");
        name = name.replace("%type%", entityName);
        name = name.replace("[", "").replace("]", "");
        meta.setDisplayName(name);

        List<String> lore = this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "lore");
        lore.replaceAll(s -> s.replace("%type%", entityName));
        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        new DropQueue(player)
                .addItem(itemStack)
                .push();
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSpawnerPlace(@NotNull final BlockPlaceEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();

        if (material != Material.SPAWNER) {
            return;
        }

        ItemStack itemStack = event.getItemInHand();
        if (!(itemStack.getItemMeta() instanceof BlockStateMeta blockStateMeta)) {
            return;
        }

        if (!blockStateMeta.getPersistentDataContainer().has(this.getPlugin().getNamespacedKeyFactory().create("softtouch"), PersistentDataType.STRING)) {
            return;
        }

        EntityType type = EntityType.valueOf(blockStateMeta.getPersistentDataContainer().get(this.getPlugin().getNamespacedKeyFactory().create("softtouch"), PersistentDataType.STRING));

        CreatureSpawner spawner = (CreatureSpawner) blockStateMeta.getBlockState();
        spawner.setSpawnedType(type);

        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        CreatureSpawner spawnerState = (CreatureSpawner) block.getState();
        spawnerState.setSpawnedType(type);
        spawnerState.update();
    }

    private String displayNicely(@NotNull final EntityType type) {
        String name = type.name().toLowerCase();
        name = name.replace("_", "");
        name = StringUtils.capitalize(name);
        return name;
    }
}
