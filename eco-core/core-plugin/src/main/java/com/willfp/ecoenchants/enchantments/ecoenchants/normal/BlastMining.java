package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.BlockUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class BlastMining extends EcoEnchant {
    public BlastMining() {
        super(
                "blast_mining", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onBlockBreak(@NotNull final Player player,
                             @NotNull final Block block,
                             final int level,
                             @NotNull final BlockBreakEvent event) {
        if (block.hasMetadata("block-ignore")) {
            return;
        }

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (player.isSneaking() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "disable-on-sneak")) {
            return;
        }

        AnticheatManager.exemptPlayer(player);

        Set<Block> toBreak = new HashSet<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "enable-sound")) {
                            block.getWorld().createExplosion(block.getLocation().clone().add(0.5, 0.5, 0.5), 0, false);
                        } else {
                            block.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, block.getLocation().clone().add(0.5, 0.5, 0.5), 1);
                        }
                        continue;
                    }
                    Block block1 = block.getWorld().getBlockAt(block.getLocation().clone().add(x, y, z));

                    if (this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blacklisted-blocks").contains(block1.getType().name().toLowerCase())) {
                        continue;
                    }

                    if (block1.getType().getHardness() > block.getType().getHardness() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "hardness-check")) {
                        continue;
                    }

                    if (block1.getType() == Material.AIR) {
                        continue;
                    }

                    if (!AntigriefManager.canBreakBlock(player, block1)) {
                        continue;
                    }

                    toBreak.add(block1);
                }
            }
        }

        ItemStack itemStack = player.getInventory().getItemInMainHand();
        ItemMeta beforeMeta = itemStack.getItemMeta();
        assert beforeMeta != null;
        boolean hadUnbreak = beforeMeta.isUnbreakable() || player.getGameMode() == GameMode.CREATIVE;
        beforeMeta.setUnbreakable(true);
        itemStack.setItemMeta(beforeMeta);
        int blocks = toBreak.size();

        toBreak.forEach((block1 -> {
            block1.setMetadata("block-ignore", this.getPlugin().getMetadataValueFactory().create(true));
            BlockUtils.breakBlock(player, block1);
            block1.removeMetadata("block-ignore", this.getPlugin());
        }));

        ItemMeta afterMeta = itemStack.getItemMeta();
        assert afterMeta != null;
        afterMeta.setUnbreakable(hadUnbreak);
        itemStack.setItemMeta(afterMeta);
        PlayerItemDamageEvent mockEvent = new PlayerItemDamageEvent(player, itemStack, blocks);
        Bukkit.getPluginManager().callEvent(mockEvent);

        if (!hadUnbreak) {
            ItemMeta wayAfterMeta = itemStack.getItemMeta();
            assert wayAfterMeta != null;
            ((Damageable) wayAfterMeta).setDamage(((Damageable) wayAfterMeta).getDamage() + mockEvent.getDamage());
            itemStack.setItemMeta(wayAfterMeta);
            if (((Damageable) wayAfterMeta).getDamage() >= itemStack.getType().getMaxDurability()) {
                PlayerItemBreakEvent breakEvent = new PlayerItemBreakEvent(player, itemStack);
                Bukkit.getPluginManager().callEvent(breakEvent);
                itemStack.setAmount(0);
            }
        }

        AnticheatManager.unexemptPlayer(player);
    }
}
