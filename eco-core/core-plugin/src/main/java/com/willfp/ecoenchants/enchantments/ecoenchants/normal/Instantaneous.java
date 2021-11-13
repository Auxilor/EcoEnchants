package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Instantaneous extends EcoEnchant {
    public Instantaneous() {
        super(
                "instantaneous", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onDamageBlock(@NotNull final Player player,
                              @NotNull final Block block,
                              final int level,
                              @NotNull final BlockDamageEvent event) {
        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        if (block.getDrops(player.getInventory().getItemInMainHand()).isEmpty()) {
            return;
        }

        if (block.getType().getHardness() > 100) {
            return;
        }

        List<Material> blacklist = new ArrayList<>();

        for (String s : this.getConfig().getStrings(EcoEnchants.CONFIG_LOCATION + "blacklisted-blocks", false)) {
            blacklist.add(Material.getMaterial(s));
        }

        if (blacklist.contains(block.getType())) {
            return;
        }

        AnticheatManager.exemptPlayer(player);

        event.setInstaBreak(true);

        AnticheatManager.unexemptPlayer(player);
    }
}
