package com.willfp.ecoenchants.firewand;


import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

public class Firewand extends Spell {
    public Firewand() {
        super("firewand");
    }

    @Override
    public void onUse(@NotNull final Player player,
                      final int level,
                      @NotNull final PlayerInteractEvent event) {
        SmallFireball fireball = player.launchProjectile(SmallFireball.class, player.getEyeLocation().getDirection().multiply(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity")));
        fireball.setIsIncendiary(this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire"));
        fireball.setMetadata("eco-damage", new FixedMetadataValue(this.getPlugin(), this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level") * level));
        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "no-explode")) {
            fireball.setMetadata("nobreak", new FixedMetadataValue(this.getPlugin(), true));
        }
        fireball.setShooter(player);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFireballDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof SmallFireball)) {
            return;
        }
        if (event.getDamager().getMetadata("eco-damage").isEmpty()) {
            return;
        }

        double multiplier = event.getDamager().getMetadata("eco-damage").get(0).asDouble();

        event.setDamage(multiplier);
    }

    @EventHandler
    public void onFireballExplode(@NotNull final EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof SmallFireball)) {
            return;
        }
        if (event.getEntity().getMetadata("nobreak").isEmpty()) {
            return;
        }

        event.setCancelled(true);
    }
}
