package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.checks.EnchantChecks;
import com.willfp.ecoenchants.nms.Target;
import com.willfp.ecoenchants.nms.TridentStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Atmospheric extends EcoEnchant {
    public Atmospheric() {
        super(
                new EcoEnchantBuilder("atmospheric", EnchantmentType.NORMAL, Target.Applicable.TRIDENT, 4.0)
        );
    }

    // START OF LISTENERS

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if(!(event.getEntity() instanceof Trident))
            return;

        if(!(event.getEntity().getShooter() instanceof Trident))
            return;

        Trident trident = (Trident) event.getEntity();
        Player player = (Player) trident.getShooter();

        if(player.isOnGround()) return;

        trident.setMetadata("shot-in-air", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Trident))
            return;

        Trident trident = (Trident) event.getDamager();
        if(!trident.hasMetadata("shot-in-air")) return;
        ItemStack item = TridentStack.getTridentStack(trident);
        if(EnchantChecks.item(item, this)) return;

        int level = EnchantChecks.getItemLevel(item, this);

        double damage = event.getDamage();
        double multiplier = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-multiplier-per-level");
        double reduction = 1 + (multiplier * level);
        event.setDamage(damage * reduction);
    }
}
