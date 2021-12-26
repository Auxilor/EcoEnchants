package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.BlockUtils;
import com.willfp.eco.util.VectorUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Drill extends EcoEnchant {
    public Drill() {
        super(
                "drill", EnchantmentType.NORMAL
        );
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

        int blocks = level * this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "blocks-per-level");

        AnticheatManager.exemptPlayer(player);

        for (int i = 1; i <= blocks; i++) {
            Vector simplified = VectorUtils.simplifyVector(player.getLocation().getDirection().normalize()).multiply(i);
            Block block1 = block.getWorld().getBlockAt(block.getLocation().clone().add(simplified));
            block1.setMetadata("block-ignore", this.getPlugin().getMetadataValueFactory().create(true));

            if (this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blacklisted-blocks", false).contains(block1.getType().name().toLowerCase())) {
                continue;
            }

            if (block1.getType().getHardness() > block.getType().getHardness() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "hardness-check")) {
                continue;
            }

            if (!AntigriefManager.canBreakBlock(player, block1)) {
                continue;
            }

            BlockUtils.breakBlock(player, block1);
            block1.removeMetadata("block-ignore", this.getPlugin());
        }

        AnticheatManager.unexemptPlayer(player);
    }
}
