package com.willfp.ecoenchants.enchantments.ecoenchants.special;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.integrations.antigrief.AntigriefManager;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Energizing extends EcoEnchant {
    public Energizing() {
        super(
                new EcoEnchantBuilder("energizing", EnchantmentType.SPECIAL, Target.Applicable.TOOL, 4.0)
        );
    }
    // START OF LISTENERS

    @EventHandler
    public void onEnergizingBreak(BlockBreakEvent event) {
        if(event.isCancelled()) return;
        Player player = event.getPlayer();
        if(!AntigriefManager.canBreakBlock(player, event.getBlock())) return;
        if(!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);

        int duration = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "ticks-per-level") * level;
        int amplifier = this.getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "initial-level") + (level - 2);

        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, duration, amplifier, true, true, true));
    }
}
