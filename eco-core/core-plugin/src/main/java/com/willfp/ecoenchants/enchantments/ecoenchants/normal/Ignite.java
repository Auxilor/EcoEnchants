package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.jetbrains.annotations.NotNull;

public class Ignite extends EcoEnchant {
    public Ignite() {
        super(
                "ignite", EnchantmentType.NORMAL
        );
    }

    @Override
    public String getPlaceholder(final int level) {
        return EnchantmentUtils.chancePlaceholder(this, level);
    }

    @Override
    public void onArrowHit(@NotNull final LivingEntity uncastShooter,
                           final int level,
                           @NotNull final ProjectileHitEvent event) {
        if (!(uncastShooter instanceof Player shooter)) {
            return;
        }

        if (event.getHitBlock() == null) {
            return;
        }

        if (!AntigriefManager.canBreakBlock(shooter, event.getHitBlock())) {
            return;
        }


        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        BlockFace face = event.getHitBlockFace();

        assert face != null;

        Block toIgnite = event.getHitBlock().getRelative(face);
        if (toIgnite.getType().equals(Material.AIR)) {
            toIgnite.setType(Material.FIRE);
        }
    }
}
