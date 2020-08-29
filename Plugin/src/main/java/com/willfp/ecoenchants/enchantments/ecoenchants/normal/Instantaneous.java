package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.anticheat.AnticheatManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;

public class Instantaneous extends EcoEnchant {
    public Instantaneous() {
        super(
                new EcoEnchantBuilder("instantaneous", EnchantmentType.NORMAL, Target.Applicable.TOOL, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onDamageBlock(BlockDamageEvent event) {
        Player player = event.getPlayer();

        if(!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        if(Rand.randFloat(0, 1) > level * 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level"))
            return;

        if(event.getBlock().getDrops(player.getInventory().getItemInMainHand()).isEmpty())
            return;

        AnticheatManager.exemptPlayer(player);

        event.setInstaBreak(true);

        AnticheatManager.unexemptPlayer(player);
    }
}