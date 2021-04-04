package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Charge extends Spell {
    public Charge() {
        super("charge");
    }

    @Override
    public boolean onUse(@NotNull final Player player,
                         final int level,
                         @NotNull final PlayerInteractEvent event) {
        Vector velocity = player.getEyeLocation().getDirection().clone();
        velocity.normalize();
        velocity.multiply(level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-per-level"));
        velocity.setY(player.getEyeLocation().getDirection().clone().getY() + 0.2);
        player.setVelocity(velocity);

        return true;
    }
}
