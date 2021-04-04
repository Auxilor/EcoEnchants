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
import org.jetbrains.annotations.NotNull;

public class Firewand extends Spell {
    public Firewand() {
        super("firewand");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                      final int level,
                      @NotNull final PlayerInteractEvent event) {
        SmallFireball fireball = player.launchProjectile(SmallFireball.class, player.getEyeLocation().getDirection().multiply(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity")));
        fireball.setIsIncendiary(this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "fire"));
        fireball.setMetadata("eco-damage", this.getPlugin().getMetadataValueFactory().create(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level") * level));
        if (this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "no-explode")) {
            fireball.setMetadata("nobreak", this.getPlugin().getMetadataValueFactory().create(true));
        }
        fireball.setShooter(player);

        return true;
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
