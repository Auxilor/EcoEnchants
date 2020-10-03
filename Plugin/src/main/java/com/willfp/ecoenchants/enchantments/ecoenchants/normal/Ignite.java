package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
public class Ignite extends EcoEnchant {
    public Ignite() {
        super(
                new EcoEnchantBuilder("ignite", EnchantmentType.NORMAL,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onArrowHit(LivingEntity uncastShooter, int level, ProjectileHitEvent event) {
        if(!(uncastShooter instanceof Player))
            return;

        if(event.getHitBlock() == null)
            return;

        Player shooter = (Player) uncastShooter;
        if (!AntigriefManager.canBreakBlock(shooter, event.getHitBlock()))
            return;


        if(!EnchantmentUtils.passedChance(this, level))
            return;

        BlockFace face = event.getHitBlockFace();

        assert face != null;

        Block toIgnite = event.getHitBlock().getRelative(face);
        if(toIgnite.getType().equals(Material.AIR)) {
            toIgnite.setType(Material.FIRE);
        }
    }
}