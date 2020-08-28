package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.EqualIfOver;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
public class Oxygenate extends EcoEnchant {
    public Oxygenate() {
        super(
                new EcoEnchantBuilder("oxygenate", EnchantmentType.NORMAL, Target.Applicable.TOOL, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void oxygenateBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!HasEnchant.playerHeld(player, this)) return;

        if (event.isCancelled())
            return;

        if (!AntigriefManager.canBreakBlock(player, block)) return;

        if(player.getRemainingAir() == player.getMaximumAir()) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        int oxygenLevel = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "oxygen-per-level");
        int oxygen = level * oxygenLevel;
        int newOxygen = player.getRemainingAir() + oxygen;
        newOxygen = EqualIfOver.equalIfOver(newOxygen, player.getMaximumAir());

        player.setRemainingAir(newOxygen);
    }
}