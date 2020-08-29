package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.util.HasEnchant;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class NetherInfusion extends EcoEnchant {
    public NetherInfusion() {
        super(
                new EcoEnchantBuilder("nether_infusion", EnchantmentType.NORMAL, Target.Applicable.SWORD, 4.01)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void netherInfusionHit(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Player))
            return;

        Player player = (Player) event.getDamager();

        if(!player.getWorld().getEnvironment().equals(World.Environment.NETHER))
            return;

        if(!HasEnchant.playerHeld(player, this)) return;

        int level = HasEnchant.getPlayerLevel(player, this);
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "per-level-multiplier");

        event.setDamage(event.getDamage() * (1 + (level * multiplier)));
    }
}
