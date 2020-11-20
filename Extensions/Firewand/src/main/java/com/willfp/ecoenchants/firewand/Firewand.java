package com.willfp.ecoenchants.firewand;

import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import com.willfp.ecoenchants.util.Logger;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Firewand extends Spell {
    public Firewand() {
        super("firewand", FirewandMain.class);
    }

    @Override
    public void onUse(Player player, int level, PlayerInteractEvent event) {
        Fireball fireball = player.launchProjectile(Fireball.class, player.getEyeLocation().getDirection().multiply(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity")));
        fireball.setIsIncendiary(false);
        fireball.setMetadata("eco-damage", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level") * level));
        if(this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "no-explode")) {
            fireball.setMetadata("nobreak", new FixedMetadataValue(EcoEnchantsPlugin.getInstance(), true));
        }
        fireball.setShooter(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFireballDamage(EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof Fireball)) return;
        if(event.getDamager().getMetadata("eco-damage").isEmpty()) return;

        double multiplier = event.getDamager().getMetadata("eco-damage").get(0).asDouble();

        event.setDamage(multiplier);
    }

    @EventHandler
    public void onFireballExplode(EntityExplodeEvent event) {
        if(!(event.getEntity() instanceof Fireball)) return;
        if(event.getEntity().getMetadata("nobreak").isEmpty()) return;

        event.setCancelled(true);
    }
}
