package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import com.willfp.ecoenchants.util.Rand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
public class BreaklessnessCurse extends EcoEnchant {
    public BreaklessnessCurse() {
        super(
                new EcoEnchantBuilder("breaklessness_curse", EnchantmentType.CURSE, Target.Applicable.TOOL, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onDamageBlock(BlockDamageEvent event) {
        Player player = event.getPlayer();

        if (!HasEnchant.playerHeld(player, this)) return;

        if (Rand.randFloat(0, 1) > 0.01 * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance"))
            return;

        event.setCancelled(true);
    }
}