package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Missile extends Spell {
    public Missile() {
        super("missile");
    }

    @Override
    public void onRightClick(Player player, int level, PlayerInteractEvent event) {
        WitherSkull skull = player.launchProjectile(WitherSkull.class, player.getEyeLocation().getDirection().multiply(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity")));
        skull.setCharged(true);
        skull.setIsIncendiary(false);
        skull.setMetadata("eco-damage", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level") * level));
        skull.setMetadata("nobreak", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(),true));
        skull.setShooter(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWitherSkullDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof WitherSkull)) return;
        if(event.getDamager().getMetadata("eco-damage").isEmpty()) return;

        double multiplier = event.getDamager().getMetadata("eco-damage").get(0).asDouble();

        event.setDamage(multiplier);
    }

    @EventHandler
    public void onWitherSkullExplode(EntityExplodeEvent event) {
        if(!(event.getEntity() instanceof WitherSkull)) return;
        if(event.getEntity().getMetadata("nobreak").isEmpty()) return;

        event.setCancelled(true);
    }
}
