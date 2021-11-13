package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.BlockUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Dynamite extends Spell {
    public Dynamite() {
        super("dynamite");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                         final int level,
                         @NotNull final PlayerInteractEvent event) {
        Block block = event.getClickedBlock();

        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return false;
        }

        if (block == null) {
            return false;
        }

        if (block.hasMetadata("block-ignore")) {
            return false;
        }

        AnticheatManager.exemptPlayer(player);

        Set<Block> toBreak = new HashSet<>();

        int baseDiff = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "base-bonus");
        int bonusPerLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "per-level-bonus");
        final int size = baseDiff + (bonusPerLevel * (level - 1));


        for (int x = -size; x <= size; x++) {
            for (int y = -size; y <= size; y++) {
                for (int z = -size; z <= size; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        block.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, block.getLocation().clone().add(0.5, 0.5, 0.5), 1);
                    }
                    Block block1 = block.getWorld().getBlockAt(block.getLocation().clone().add(x, y, z));

                    if (this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blacklisted-blocks", false).contains(block1.getType().name().toLowerCase())) {
                        continue;
                    }

                    if (block1.getType().getHardness() > block.getType().getHardness() && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "hardness-check")) {
                        continue;
                    }

                    if (!AntigriefManager.canBreakBlock(player, block1)) {
                        continue;
                    }

                    toBreak.add(block1);
                }
            }
        }

        toBreak.forEach((block1 -> {
            block1.setMetadata("block-ignore", this.getPlugin().getMetadataValueFactory().create(true));
            BlockUtils.breakBlock(player, block1);
            block1.removeMetadata("block-ignore", this.getPlugin());
        }));

        AnticheatManager.unexemptPlayer(player);

        return true;
    }

    @Override
    protected boolean requiresBlockClick() {
        return true;
    }
}
