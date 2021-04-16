package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class Missile extends Spell {
    public Missile() {
        super("missile");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                         final int level,
                         @NotNull final PlayerInteractEvent event) {
        WitherSkull skull = player.launchProjectile(WitherSkull.class, player.getEyeLocation().getDirection().multiply(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity")));
        skull.setCharged(true);
        skull.setIsIncendiary(false);
        skull.setMetadata("eco-damage", this.getPlugin().getMetadataValueFactory().create(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "damage-per-level") * level));
        skull.setMetadata("nobreak", this.getPlugin().getMetadataValueFactory().create(true));
        skull.setShooter(player);

        return true;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWitherSkullDamage(@NotNull final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof WitherSkull)) {
            return;
        }

        if (event.getDamager().getMetadata("eco-damage").isEmpty()) {
            return;
        }

        double multiplier = event.getDamager().getMetadata("eco-damage").get(0).asDouble();

        if (((WitherSkull) event.getDamager()).getShooter().equals(event.getEntity())) {
            event.setCancelled(true);
        }

        event.setDamage(multiplier);
    }

    @EventHandler
    public void onWitherSkullExplode(@NotNull final EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof WitherSkull)) {
            return;
        }

        if (event.getEntity().getMetadata("nobreak").isEmpty()) {
            return;
        }

        event.setCancelled(true);
    }
}
