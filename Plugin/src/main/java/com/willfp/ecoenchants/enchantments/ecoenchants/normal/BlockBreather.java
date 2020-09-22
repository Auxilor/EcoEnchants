package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class BlockBreather extends EcoEnchant {
    public BlockBreather() {
        super(
                new EcoEnchantBuilder("block_breather", EnchantmentType.NORMAL, 5.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onHurt(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        if(!event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION))
            return;

        Player player = (Player) event.getEntity();

        if(!EnchantChecks.helmet(player, this)) return;
        int level = EnchantChecks.getHelmetLevel(player, this);

        double chance = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");

        if (NumberUtils.randFloat(0, 1) > level * 0.01 * chance)
            return;

        event.setCancelled(true);
    }

}
