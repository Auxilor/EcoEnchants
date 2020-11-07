package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class Charge extends Spell {
    public Charge() {
        super("charge");
    }

    @Override
    public void onRightClick(Player player, int level, PlayerInteractEvent event) {
        Vector velocity = player.getEyeLocation().getDirection().clone();
        velocity.multiply(level * this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-per-level"));
        velocity.setY(0.2);
        player.setVelocity(velocity);
    }
}
