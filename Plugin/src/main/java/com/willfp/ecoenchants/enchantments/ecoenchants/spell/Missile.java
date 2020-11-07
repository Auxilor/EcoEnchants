package com.willfp.ecoenchants.enchantments.ecoenchants.spell;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.itemtypes.Spell;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;

public class Missile extends Spell {
    public Missile() {
        super("missile");
    }

    @Override
    public void onRightClick(Player player, int level) {
        WitherSkull skull = player.launchProjectile(WitherSkull.class, player.getEyeLocation().getDirection().multiply(this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity")));
        skull.setCharged(true);
        skull.setIsIncendiary(false);
        skull.setYield((float) this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "power"));
        skull.setShooter(player);
    }
}
