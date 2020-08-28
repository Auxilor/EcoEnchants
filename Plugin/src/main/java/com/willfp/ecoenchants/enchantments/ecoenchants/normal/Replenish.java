package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.Main;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
public class Replenish extends EcoEnchant {
    public Replenish() {
        super(
                new EcoEnchantBuilder("replenish", EnchantmentType.NORMAL, Target.Applicable.HOE, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onCropBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material type = block.getType();

        if(!HasEnchant.playerHeld(player, this)) return;

        if(!AntigriefManager.canBreakBlock(player, block)) return;
        if(event.isCancelled()) return;

        if(!(block.getBlockData() instanceof Ageable)) return;

        Ageable data = (Ageable) block.getBlockData();
        if(data.getAge() != data.getMaximumAge()) return;

        data.setAge(0);


        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(type);
                block.setBlockData(data);
            }
        }.runTaskLater(Main.getInstance(), 1);
    }
}
